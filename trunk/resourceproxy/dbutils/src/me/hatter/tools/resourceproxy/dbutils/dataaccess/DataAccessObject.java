package me.hatter.tools.resourceproxy.dbutils.dataaccess;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import me.hatter.tools.resourceproxy.dbutils.factory.ConnectionPool;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;
import me.hatter.tools.resourceproxy.dbutils.util.ReflectUtil;

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
        try {
            return execute.execute(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    public static void insertObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateInsertSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
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
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                preparedStatement.execute();
                return null;
            }
        });
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
                PreparedStatement preparedStatement = connection.prepareStatement(runSql);
                if (objectList != null) {
                    for (int i = 0; i < objectList.size(); i++) {
                        int index = i + 1;
                        Object o = objectList;
                        Class<?> type = (o == null) ? null : o.getClass();
                        setPreparedStatmentByValue(preparedStatement, index, type, o);
                    }
                }
                List<T> result = new ArrayList<T>();
                List<String> fieldList = DBUtil.getTableFields(clazz);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    T o = clazz.newInstance();
                    for (String f : fieldList) {
                        Field field = ReflectUtil.getField(clazz, f);
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
                        result.add(o);
                    }
                }
                return null;
            }
        });
    }

    private static void setPreparedStatmentValues(PreparedStatement preparedStatement, Class<?> clazz, Object object,
                                                  List<String> refFieldList) throws Exception {
        for (int i = 0; i < refFieldList.size(); i++) {
            int index = i + 1;
            AtomicReference<Class<?>> refType = new AtomicReference<Class<?>>();
            Object o = ReflectUtil.getFieldValue(clazz, object, refFieldList.get(i), refType);
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
        } else if (type == Date.class) {
            preparedStatement.setDate(index, new java.sql.Date(((Date) o).getTime()));
        } else {
            throw new RuntimeException("Unsupoort ed type: " + type);
        }
    }

    //
    // public static AccessItem getAccessItem(final String ip, final String url) {
    // return execute(new Execute<AccessItem>() {
    //
    // @Override
    // public AccessItem execute(Connection connection) throws Exception {
    // PreparedStatement preparedStatement =
    // connection.prepareStatement("select * from access_table where ip=? and url=?");
    // preparedStatement.setString(1, ip);
    // preparedStatement.setString(2, url);
    // ResultSet resultSet = preparedStatement.executeQuery();
    //
    // AccessItem accessItem = null;
    // if (resultSet.next()) {
    // accessItem = new AccessItem();
    // accessItem.setId(resultSet.getInt("id"));
    // accessItem.setIp(resultSet.getString("ip"));
    // accessItem.setCreated(getSDF().parse(resultSet.getString("created")));
    // accessItem.setModified(getSDF().parse(resultSet.getString("modified")));
    // accessItem.setAccess_count(resultSet.getInt("access_count"));
    // accessItem.setUrl(resultSet.getString("url"));
    // accessItem.setStatus(resultSet.getInt("status"));
    // accessItem.setHeader(resultSet.getString("header"));
    // accessItem.setContent(resultSet.getString("content"));
    // }
    //
    // return accessItem;
    // }
    // });
    // }
}
