package me.hatter.tools.resourceproxy.dbutils.dataaccess;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.commons.util.ReflectUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.factory.ConnectionPool;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;

//
public class DataAccessObject {

    private static final ConnectionPool connectionPool = new ConnectionPool();

    // public static void main(String[] args) {
    // AccessItem ai = getAccessItem("1.1.1.1", "http://sample.com/");
    // System.out.println(ai.getId());
    // System.out.println(ai.getContent());
    // System.out.println(ai.getHeader());
    // }
    //
    public static interface Execute<T> {

        T execute(Connection connection) throws Exception;
    }

    protected static <T> T execute(Execute<T> execute) {
        Connection connection = connectionPool.borrowConnection();
        boolean hasError = false;
        try {
            return execute.execute(connection);
        } catch (Exception e) {
            hasError = true;
            throw new RuntimeException(e);
        } finally {
            try {
                if (hasError) {
                    connectionPool.returnConnectionWithError(connection);
                } else {
                    connectionPool.returnConnection(connection);
                }
            } catch (Exception ex) {
                System.out.println("[ERROR] error when return connection with flag: " + hasError + " "
                                   + StringUtil.printStackTrace(ex));
            }
        }
    }

    public static void insertObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateInsertSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                System.out.println("[INFO] insert sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                preparedStatement.execute();
                return null;
            }
        });
    }

    public static void updateObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateUpdateSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                System.out.println("[INFO] update sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                preparedStatement.execute();
                return null;
            }
        });
    }

    public static void deleteObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateDeleteSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                System.out.println("[INFO] delete sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                preparedStatement.execute();
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> T selectObject(final T object) {
        final Class<T> clazz = (Class<T>) object.getClass();
        List<String> pkList = DBUtil.getPkList(clazz);
        final List<Object> pkVList = new ArrayList<Object>();
        String where = StringUtil.join(CollUtil.transform(pkList, new CollUtil.Transformer<String, String>() {

            @Override
            public String transform(String object2) {
                Object o = ReflectUtil.getFieldValue(clazz, object, StringUtil.toCamel(object2),
                                                     new AtomicReference<Class<?>>());
                pkVList.add(o);
                return object2 + " = ?";
            }
        }), " and ");
        List<T> resultList = listObjects(clazz, where, pkVList);
        if ((resultList == null) || (resultList.isEmpty())) {
            return null;
        }
        return resultList.get(0);
    }

    public static int executeSql(final String sql, final List<Object> objectList) {
        return execute(new Execute<Integer>() {

            @Override
            public Integer execute(Connection connection) throws Exception {
                System.out.println("[INFO] query sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                if (objectList != null) {
                    for (int i = 0; i < objectList.size(); i++) {
                        int index = i + 1;
                        Object o = objectList.get(i);
                        System.out.println("[INFO] object @" + index + "=" + o);
                        Class<?> type = (o == null) ? null : o.getClass();
                        setPreparedStatmentByValue(preparedStatement, index, type, o);
                    }
                }
                return preparedStatement.executeUpdate();
            }
        });
    }

    public static <T> int countObject(Class<T> clazz, String where, List<Object> objectList) {
        String sql = "select count(*) count from " + DBUtil.getTableName(clazz) + " where " + where;
        List<Count> countList = listObjects(Count.class, sql, objectList);
        return ((countList == null) || countList.isEmpty()) ? 0 : countList.get(0).getCount().intValue();
    }

    public static <T> List<T> listObjects(final Class<T> clazz, String where, final List<Object> objectList) {
        String sql = null;
        if (where.trim().toUpperCase().startsWith("SELECT")) {
            sql = where;
        } else {
            sql = "select * from " + DBUtil.getTableName(clazz) + " where " + where;
        }
        final String runSql = sql;
        return execute(new Execute<List<T>>() {

            @Override
            public List<T> execute(Connection connection) throws Exception {
                System.out.println("[INFO] query sql: " + runSql);
                PreparedStatement preparedStatement = connection.prepareStatement(runSql);
                if (objectList != null) {
                    for (int i = 0; i < objectList.size(); i++) {
                        int index = i + 1;
                        Object o = objectList.get(i);
                        System.out.println("[INFO] object @" + index + "=" + o);
                        Class<?> type = (o == null) ? null : o.getClass();
                        setPreparedStatmentByValue(preparedStatement, index, type, o);
                    }
                }
                List<T> result = new ArrayList<T>();
                List<String> fieldList = DBUtil.getTableFields(clazz);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    T o = clazz.newInstance();
                    for (String f : fieldList) {
                        Field field = ReflectUtil.getField(clazz, StringUtil.toCamel(f));
                        field.setAccessible(true);
                        if (field.getType() == String.class) {
                            field.set(o, resultSet.getString(f));
                        } else if (field.getType() == Integer.class) {
                            field.set(o, resultSet.getInt(f));
                        } else if (field.getType() == Date.class) {
                            java.sql.Date d = resultSet.getDate(f);
                            field.set(o, (d == null) ? null : new Date(d.getTime()));
                        } else {
                            throw new RuntimeException("Unsupoort ed type: " + field.getType());
                        }
                    }
                    result.add(o);
                }
                return result;
            }
        });
    }

    private static void setPreparedStatmentValues(PreparedStatement preparedStatement, Class<?> clazz, Object object,
                                                  List<String> refFieldList) throws Exception {
        for (int i = 0; i < refFieldList.size(); i++) {
            int index = i + 1;
            AtomicReference<Class<?>> refType = new AtomicReference<Class<?>>();
            Object o = ReflectUtil.getFieldValue(clazz, object, StringUtil.toCamel(refFieldList.get(i)), refType);
            System.out.println("[INFO] object @" + index + "=" + o);
            setPreparedStatmentByValue(preparedStatement, index, refType.get(), o);
        }
    }

    private static void setPreparedStatmentByValue(PreparedStatement preparedStatement, int index, Class<?> type,
                                                   Object o) throws SQLException {
        if (o == null) {
            preparedStatement.setObject(index, null);
        } else if (type == String.class) {
            preparedStatement.setString(index, (String) o);
        } else if (type == Integer.class) {
            preparedStatement.setInt(index, (Integer) o);
        } else if (type == Byte.class) {
            preparedStatement.setByte(index, (Byte) o);
        } else if (type == Short.class) {
            preparedStatement.setShort(index, (Short) o);
        } else if (type == Long.class) {
            preparedStatement.setLong(index, (Long) o);
        } else if (type == Float.class) {
            preparedStatement.setFloat(index, (Float) o);
        } else if (type == Double.class) {
            preparedStatement.setDouble(index, (Double) o);
        } else if (type == Date.class) {
            preparedStatement.setDate(index, new java.sql.Date(((Date) o).getTime()));
        } else if (type == BigDecimal.class) {
            preparedStatement.setBigDecimal(index, (BigDecimal) o);
        } else {
            throw new RuntimeException("Unsupoorted type: " + type);
        }
    }
}
