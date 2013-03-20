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
import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.factory.ConnectionFactory;
import me.hatter.tools.resourceproxy.dbutils.factory.ConnectionPool;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;

//
public class DataAccessObject {

    private ConnectionPool connectionPool;
    private Connection     _connection;
    private Exception      _connectionError;
    private boolean        logging = true;

    public DataAccessObject(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public DataAccessObject(ConnectionFactory connectionFactory) {
        this(new ConnectionPool(connectionFactory));
    }

    public DataAccessObject(PropertyConfig propertyConfig) {
        this(new ConnectionFactory(propertyConfig));
    }

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public static interface Execute<T> {

        T execute(Connection connection) throws Exception;
    }

    protected <T> T execute(Execute<T> execute) {
        Connection connection = (_connection != null) ? _connection : connectionPool.borrowConnection();
        boolean hasError = false;
        try {
            return execute.execute(connection);
        } catch (Exception e) {
            if ((_connection != null) && (_connectionError == null)) {
                _connectionError = e;
            }
            hasError = true;
            throw new RuntimeException(e);
        } finally {
            if (_connection == null) {
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
    }

    public DataAccessObject borrowAsDataAccessObject() {
        DataAccessObject dataAccessObject = new DataAccessObject(this.connectionPool);
        dataAccessObject._connection = this.connectionPool.borrowConnection();
        return dataAccessObject;
    }

    public void returnDataAccessObject() {
        if (this._connection != null) {
            if (this._connectionError != null) {
                this.connectionPool.returnConnectionWithError(_connection);
            } else {
                this.connectionPool.returnConnection(_connection);
            }
            this._connectionError = null;
        }
    }

    public Connection getConnection() {
        if (_connection == null) throw new RuntimeException("Not run in connection pin mode.");
        return _connection;
    }

    public void insertObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateInsertSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] insert sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                preparedStatement.execute();
                preparedStatement.close();
                return null;
            }
        });
    }

    public void insertObjectList(final List<? extends Object> objectList) {
        if ((objectList == null) || (objectList.isEmpty())) return;
        final Class<?> clazz = objectList.get(0).getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateInsertSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] insert sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for (Object object : objectList) {
                    if (clazz != object.getClass()) {
                        throw new RuntimeException("Class not match: " + clazz.getName() + " & " + object.getClass());
                    }
                    setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                    preparedStatement.execute();
                }
                preparedStatement.close();
                return null;
            }
        });
    }

    public void updateObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateUpdateSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] update sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                preparedStatement.execute();
                preparedStatement.close();
                return null;
            }
        });
    }

    public void updateObjectList(final List<? extends Object> objectList) {
        if ((objectList == null) || (objectList.isEmpty())) return;
        final Class<?> clazz = objectList.get(0).getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateUpdateSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] update sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for (Object object : objectList) {
                    if (clazz != object.getClass()) {
                        throw new RuntimeException("Class not match: " + clazz.getName() + " & " + object.getClass());
                    }
                    setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                    preparedStatement.execute();
                }
                preparedStatement.close();
                return null;
            }
        });
    }

    public <T> void clearTable(final Class<T> clazz) {
        final String sql = "delete from " + DBUtil.getTableName(clazz);
        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] delete sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
                preparedStatement.close();
                return null;
            }
        });
    }

    public void deleteObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateDeleteSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] delete sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                preparedStatement.execute();
                preparedStatement.close();
                return null;
            }
        });
    }

    public void deleteObjectList(final List<? extends Object> objectList) {
        if ((objectList == null) || (objectList.isEmpty())) return;
        final Class<?> clazz = objectList.get(0).getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateDeleteSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            @Override
            public Void execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] delete sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                for (Object object : objectList) {
                    if (clazz != object.getClass()) {
                        throw new RuntimeException("Class not match: " + clazz.getName() + " & " + object.getClass());
                    }
                    setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                    preparedStatement.execute();
                }
                preparedStatement.close();
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> T selectObject(final T object) {
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

    public int executeSql(final String sql, final List<Object> objectList) {
        return execute(new Execute<Integer>() {

            @Override
            public Integer execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] query sql: " + sql);
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
                int result = preparedStatement.executeUpdate();
                preparedStatement.close();
                return result;
            }
        });
    }

    public <T> int countObject(Class<T> clazz, String where, List<Object> objectList) {
        String sql = makeCountSQL(clazz, where);
        List<Count> countList = listObjects(Count.class, sql, objectList);
        return ((countList == null) || countList.isEmpty()) ? 0 : countList.get(0).getCount().intValue();
    }

    public <T> T findObject(final Class<T> clazz) {
        return first(listObjects(clazz));
    }

    public <T> List<T> listObjects(final Class<T> clazz) {
        return listObjects(clazz, null, null);
    }

    public <T> T findObject(final Class<T> clazz, String orderBy) {
        return first(listObjects(clazz, orderBy));
    }

    public <T> List<T> listObjects(final Class<T> clazz, String orderBy) {
        return listObjects(clazz, null, orderBy, null);
    }

    public <T> T findObject(final Class<T> clazz, String where, final List<Object> objectList) {
        return first(listObjects(clazz, where, objectList));
    }

    public <T> List<T> listObjects(final Class<T> clazz, String where, final List<Object> objectList) {
        return listObjects(clazz, where, null, objectList);
    }

    public <T> T findObject(final Class<T> clazz, String where, String orderBy, final List<Object> objectList) {
        return first(listObjects(clazz, where, orderBy, objectList));
    }

    public <T> List<T> listObjects(final Class<T> clazz, String where, String orderBy, final List<Object> objectList) {
        final String runSql = makeSQL(clazz, where, orderBy);
        return execute(new Execute<List<T>>() {

            @Override
            public List<T> execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] query sql: " + runSql);
                PreparedStatement preparedStatement = connection.prepareStatement(runSql);
                if (objectList != null) {
                    for (int i = 0; i < objectList.size(); i++) {
                        int index = i + 1;
                        Object o = objectList.get(i);
                        if (logging) System.out.println("[INFO] object @" + index + "=" + o);
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
                        setFiledByResultSet(resultSet, o, f, field);
                    }
                    result.add(o);
                }
                resultSet.close();
                preparedStatement.close();
                return result;
            }
        });
    }

    public <T> void iterateObjects(final Class<T> clazz, String where, final List<Object> objectList,
                                   final RecordProcessor<T> recordProcessor) {
        iterateObjects(clazz, where, null, objectList, recordProcessor);
    }

    public <T> void iterateObjects(final Class<T> clazz, String where, String orderBy, final List<Object> objectList,
                                   final RecordProcessor<T> recordProcessor) {
        final String runSql = makeSQL(clazz, where, orderBy);
        execute(new Execute<List<T>>() {

            @Override
            public List<T> execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] query sql: " + runSql);
                PreparedStatement preparedStatement = connection.prepareStatement(runSql);
                if (objectList != null) {
                    for (int i = 0; i < objectList.size(); i++) {
                        int index = i + 1;
                        Object o = objectList.get(i);
                        if (logging) System.out.println("[INFO] object @" + index + "=" + o);
                        Class<?> type = (o == null) ? null : o.getClass();
                        setPreparedStatmentByValue(preparedStatement, index, type, o);
                    }
                }
                List<T> result = new ArrayList<T>();
                List<String> fieldList = DBUtil.getTableFields(clazz);
                ResultSet resultSet = preparedStatement.executeQuery();

                int index = 0;
                while (resultSet.next()) {
                    T o = clazz.newInstance();
                    for (String f : fieldList) {
                        Field field = ReflectUtil.getField(clazz, StringUtil.toCamel(f));
                        field.setAccessible(true);
                        setFiledByResultSet(resultSet, o, f, field);
                    }
                    recordProcessor.process(index, o);
                    index++;
                }
                resultSet.close();
                preparedStatement.close();
                return result;
            }

        });
    }

    public void iterateResultSet(final Class<?> clazz, String where, final List<Object> objectList,
                                 final RecordProcessor<ResultSet> recordProcessor) {
        iterateResultSet(clazz, where, null, objectList, recordProcessor);
    }

    public void iterateResultSet(final Class<?> clazz, String where, String orderBy, final List<Object> objectList,
                                 final RecordProcessor<ResultSet> recordProcessor) {
        final String runSql = makeSQL(clazz, where, orderBy);
        execute(new Execute<List<?>>() {

            @Override
            public List<?> execute(Connection connection) throws Exception {
                if (logging) System.out.println("[INFO] query sql: " + runSql);
                PreparedStatement preparedStatement = connection.prepareStatement(runSql);
                if (objectList != null) {
                    for (int i = 0; i < objectList.size(); i++) {
                        int index = i + 1;
                        Object o = objectList.get(i);
                        if (logging) System.out.println("[INFO] object @" + index + "=" + o);
                        Class<?> type = (o == null) ? null : o.getClass();
                        setPreparedStatmentByValue(preparedStatement, index, type, o);
                    }
                }
                List<?> result = new ArrayList<Object>();
                ResultSet resultSet = preparedStatement.executeQuery();

                int index = 0;
                while (resultSet.next()) {
                    recordProcessor.process(index, resultSet);
                    index++;
                }
                preparedStatement.close();
                return result;
            }

        });
    }

    private <T> String makeCountSQL(Class<T> clazz, String where) {
        String sql = "select count(*) count from " + DBUtil.getTableName(clazz);
        if ((where != null) && (where.trim().length() > 0)) {
            sql += " where " + where;
        }
        return sql;
    }

    private String makeSQL(Class<?> clazz, String where, String orderBy) {
        if (where != null) {
            if (where.trim().toUpperCase().startsWith("SELECT")) {
                return where;
            } else if (where.trim().toUpperCase().startsWith("!")) {
                return where.substring(1);
            }
        }
        String sql = "select * from " + DBUtil.getTableName(clazz);
        if ((where != null) && (where.trim().length() > 0)) {
            sql += " where " + where;
        }
        if ((orderBy != null) && (orderBy.trim().length() > 0)) {
            sql += " order by " + orderBy;
        }
        return sql;
    }

    private <T> T first(List<T> list) {
        if ((list == null) || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private void setPreparedStatmentValues(PreparedStatement preparedStatement, Class<?> clazz, Object object,
                                           List<String> refFieldList) throws Exception {
        for (int i = 0; i < refFieldList.size(); i++) {
            int index = i + 1;
            AtomicReference<Class<?>> refType = new AtomicReference<Class<?>>();
            Object o = ReflectUtil.getFieldValue(clazz, object, StringUtil.toCamel(refFieldList.get(i)), refType);
            if (logging) System.out.println("[INFO] object @" + index + "=" + o);
            setPreparedStatmentByValue(preparedStatement, index, refType.get(), o);
        }
    }

    private void setFiledByResultSet(ResultSet resultSet, Object o, String f, Field field)
                                                                                          throws IllegalAccessException,
                                                                                          SQLException {
        if (field.getType() == String.class) {
            field.set(o, resultSet.getString(f));
        } else if (field.getType() == Integer.class) {
            field.set(o, resultSet.getInt(f));
        } else if (field.getType() == Byte.class) {
            field.set(o, resultSet.getByte(f));
        } else if (field.getType() == Short.class) {
            field.set(o, resultSet.getShort(f));
        } else if (field.getType() == Long.class) {
            field.set(o, resultSet.getLong(f));
        } else if (field.getType() == Float.class) {
            field.set(o, resultSet.getFloat(f));
        } else if (field.getType() == Double.class) {
            field.set(o, resultSet.getDouble(f));
        } else if (field.getType() == Date.class) {
            java.sql.Date d = resultSet.getDate(f);
            field.set(o, (d == null) ? null : new Date(d.getTime()));
        } else if (field.getType() == BigDecimal.class) {
            field.set(o, resultSet.getBigDecimal(f));
        } else if (field.getType() == Boolean.class) {
            field.set(o, resultSet.getBoolean(f));
        } else {
            throw new RuntimeException("Unsupoorted type: " + field.getType());
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
        } else if (type == Boolean.class) {
            preparedStatement.setBoolean(index, (Boolean) o);
        } else {
            throw new RuntimeException("Unsupoorted type: " + type);
        }
    }
}
