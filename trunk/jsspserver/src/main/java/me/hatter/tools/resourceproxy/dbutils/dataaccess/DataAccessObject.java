package me.hatter.tools.resourceproxy.dbutils.dataaccess;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.commons.util.ReflectUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.factory.ConnectionFactory;
import me.hatter.tools.resourceproxy.dbutils.factory.ConnectionPool;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;

public class DataAccessObject {

    private static final LogTool logTool              = LogTools.getLogTool(DataAccessObject.class);

    protected ConnectionPool     connectionPool;
    protected Connection         _connection;
    protected AtomicInteger      _connectionExecCount = new AtomicInteger(0);
    protected Exception          _connectionError;
    protected boolean            logging              = !"false".equalsIgnoreCase(System.getProperty("dataAccessObject.logging"));
    protected long               loggingMillis        = Long.parseLong(System.getProperty("dataAccessObject.loggingMillis",
                                                                                          "200"));

    public DataAccessObject(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public DataAccessObject(ConnectionFactory connectionFactory) {
        this(new ConnectionPool(connectionFactory));
    }

    public DataAccessObject(PropertyConfig propertyConfig) {
        this(new ConnectionFactory(propertyConfig));
    }

    public long getLoggingMillis() {
        return loggingMillis;
    }

    public void setLoggingMillis(long loggingMillis) {
        this.loggingMillis = loggingMillis;
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
        long start = System.currentTimeMillis();
        Connection connection = (_connection != null) ? _connection : connectionPool.borrowConnection();
        long borrowEnd = System.currentTimeMillis();
        boolean hasError = false;
        try {
            T result = execute.execute(connection);
            long end = System.currentTimeMillis();
            long cost = end - start;
            if (cost >= loggingMillis) {
                if (logTool.isWarnEnable()) {
                    logTool.warn("Sql runing too long; total cost millis: " + cost + ", borrow cost millis: "
                                 + (borrowEnd - start) + ", sql execute cost millis: " + (end - borrowEnd));
                }
            }
            return result;
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
                    logTool.error("error when return connection with flag: " + hasError, ex);
                }
            } else {
                _connectionExecCount.incrementAndGet();
            }
        }
    }

    public DataAccessObject borrowAsDataAccessObject() {
        DataAccessObject dataAccessObject = new DataAccessObject(this.connectionPool);
        dataAccessObject.setLogging(this.isLogging());
        dataAccessObject.setLoggingMillis(this.getLoggingMillis());
        dataAccessObject._connection = this.connectionPool.borrowConnection();
        return dataAccessObject;
    }

    public <T extends DataAccessObject> T borrowAsDataAccessObject(Class<T> daoClass) {
        try {
            Constructor<T> daoConstructor = daoClass.getConstructor(new Class[] { ConnectionPool.class });
            T dao = daoConstructor.newInstance(new Object[] { this.connectionPool });
            dao.setLogging(this.isLogging());
            dao.setLoggingMillis(this.getLoggingMillis());
            Field _connection = DataAccessObject.class.getDeclaredField("_connection");
            _connection.setAccessible(true);
            _connection.set(dao, this.connectionPool.borrowConnection());
            return dao;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public int getConnectionExecCount() {
        return _connectionExecCount.intValue();
    }

    public void insertOrUpdateObject(final Object object) {
        final Class<?> clazz = object.getClass();
        List<String> pkList = DBUtil.getPkList(clazz);
        if ((pkList == null) || pkList.isEmpty()) {
            throw new RuntimeException("No PK found in class: " + clazz);
        }
        if (selectObject(object) == null) {
            insertObject(object);
        } else {
            updateObject(object);
        }
    }

    public void insertOrUpdateObjectList(final List<? extends Object> objectList) {
        if ((objectList == null) || objectList.isEmpty()) {
            return;
        }
        for (Object object : objectList) {
            insertObject(object);
        }
    }

    public Integer insertObjectAndGetSqliteLastId(final Object object) {
        return insertObjectAndGetLastId(object, "select last_insert_rowid() id");
    }

    public Integer insertObjectAndGetMySQLLastId(final Object object) {
        return insertObjectAndGetLastId(object, "select last_insert_id() id");
    }

    public Integer insertObjectAndGetLastId(final Object object, final String lastIdQuery) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateInsertSQL(clazz, refFieldList);

        return execute(new Execute<Integer>() {

            // @Override
            public Integer execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Insert sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                try {
                    setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                    preparedStatement.execute();
                } finally {
                    preparedStatement.close();
                }

                PreparedStatement lastInsertRowidPreparedStatement = connection.prepareStatement(lastIdQuery);
                Integer lastId = null;
                try {
                    ResultSet resultSet = lastInsertRowidPreparedStatement.executeQuery();
                    try {
                        if (resultSet.next()) {
                            lastId = resultSet.getInt("id");
                        }
                    } finally {
                        resultSet.close();
                    }
                } finally {
                    lastInsertRowidPreparedStatement.close();
                }
                return lastId;
            }
        });
    }

    public void insertObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateInsertSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            // @Override
            public Void execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Insert sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                try {
                    setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                    preparedStatement.execute();
                } finally {
                    preparedStatement.close();
                }
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

            // @Override
            public Void execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Insert sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                try {
                    for (Object object : objectList) {
                        if (clazz != object.getClass()) {
                            throw new RuntimeException("Class not match: " + clazz.getName() + " & "
                                                       + object.getClass());
                        }
                        setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                        preparedStatement.execute();
                    }
                } finally {
                    preparedStatement.close();
                }
                return null;
            }
        });
    }

    public void updateObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateUpdateSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            // @Override
            public Void execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Update sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                try {
                    setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                    preparedStatement.execute();
                } finally {
                    preparedStatement.close();
                }
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

            // @Override
            public Void execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Update sql: " + sql);
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

            // @Override
            public Void execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Delete sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                try {
                    preparedStatement.execute();
                } finally {
                    preparedStatement.close();
                }
                return null;
            }
        });
    }

    public void deleteObject(final Object object) {
        final Class<?> clazz = object.getClass();
        final List<String> refFieldList = new ArrayList<String>();
        final String sql = DBUtil.generateDeleteSQL(clazz, refFieldList);

        execute(new Execute<Void>() {

            // @Override
            public Void execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Delete sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                try {
                    setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                    preparedStatement.execute();
                } finally {
                    preparedStatement.close();
                }
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

            // @Override
            public Void execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Delete sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                try {
                    for (Object object : objectList) {
                        if (clazz != object.getClass()) {
                            throw new RuntimeException("Class not match: " + clazz.getName() + " & "
                                                       + object.getClass());
                        }
                        setPreparedStatmentValues(preparedStatement, clazz, object, refFieldList);
                        preparedStatement.execute();
                    }
                } finally {
                    preparedStatement.close();
                }
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

            // @Override
            public String transform(String object2) {
                Object o = ReflectUtil.getFieldValue(clazz, object, DBUtil.getClassFieldByDbName(clazz, object2),
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

            // @Override
            public Integer execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Query sql: " + sql);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                int result = 0;
                try {
                    if (objectList != null) {
                        for (int i = 0; i < objectList.size(); i++) {
                            int index = i + 1;
                            Object o = objectList.get(i);
                            if (logging && logTool.isInfoEnable()) logTool.info("Object @" + index + "=" + o);
                            Class<?> type = (o == null) ? null : o.getClass();
                            setPreparedStatmentByValue(preparedStatement, index, type, o);
                        }
                    }
                    result = preparedStatement.executeUpdate();
                } finally {
                    preparedStatement.close();
                }
                return result;
            }
        });
    }

    public int getSqliteLastId() {
        String sql = "select last_insert_rowid() id";
        LastId lastId = findObject(LastId.class, sql, null);
        return (lastId == null) ? 0 : lastId.getId().intValue();
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

            // @Override
            public List<T> execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Query sql: " + runSql);
                PreparedStatement preparedStatement = connection.prepareStatement(runSql);
                List<T> result = null;
                try {
                    if (objectList != null) {
                        for (int i = 0; i < objectList.size(); i++) {
                            int index = i + 1;
                            Object o = objectList.get(i);
                            if (logging && logTool.isInfoEnable()) logTool.info("Object @" + index + "=" + o);
                            Class<?> type = (o == null) ? null : o.getClass();
                            setPreparedStatmentByValue(preparedStatement, index, type, o);
                        }
                    }
                    result = new ArrayList<T>();
                    List<String> fieldList = DBUtil.getTableFields(clazz);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    try {
                        while (resultSet.next()) {
                            T o = clazz.newInstance();
                            for (String f : fieldList) {
                                Field field = DBUtil.getClassFieldByDbName(clazz, f);
                                field.setAccessible(true);
                                setFiledByResultSet(resultSet, o, f, field);
                            }
                            result.add(o);
                        }
                    } finally {
                        resultSet.close();
                    }
                } finally {
                    preparedStatement.close();
                }
                return result;
            }
        });
    }

    public <T> T findSingleObject(final Class<T> clazz, String query, final List<Object> objectList) {
        return first(listSingleObjects(clazz, query, objectList));
    }

    public <T> List<T> listSingleObjects(final Class<T> clazz, String query, final List<Object> objectList) {
        if (StringUtil.isEmpty(query)) {
            throw new IllegalArgumentException("Query cannot be empty.");
        }
        final List<T> list = new ArrayList<T>();
        iterateResultSet(null, query, objectList, new RecordProcessor<ResultSet>() {

            // @Override
            public void process(int index, ResultSet record) throws Exception {
                if (clazz == String.class) {
                    list.add(clazz.cast(record.getString(0)));
                } else if (clazz == Integer.class) {
                    list.add(clazz.cast(record.getInt(0)));
                } else if (clazz == Byte.class) {
                    list.add(clazz.cast(record.getByte(0)));
                } else if (clazz == Short.class) {
                    list.add(clazz.cast(record.getShort(0)));
                } else if (clazz == Long.class) {
                    list.add(clazz.cast(record.getLong(0)));
                } else if (clazz == Float.class) {
                    list.add(clazz.cast(record.getFloat(0)));
                } else if (clazz == Double.class) {
                    list.add(clazz.cast(record.getDouble(0)));
                } else if (clazz == Date.class) {
                    java.sql.Date d = record.getDate(0);
                    list.add(clazz.cast((d == null) ? null : new Date(d.getTime())));
                } else if (clazz == BigDecimal.class) {
                    list.add(clazz.cast(record.getBigDecimal(0)));
                } else if (clazz == Boolean.class) {
                    list.add(clazz.cast(record.getBoolean(0)));
                } else {
                    throw new RuntimeException("Unsupoorted type: " + clazz);
                }
            }
        });
        return list;
    }

    public List<Map<String, Object>> listMapList(String query, final List<Object> objectList) {
        if (StringUtil.isEmpty(query)) {
            throw new IllegalArgumentException("Query cannot be empty.");
        }
        final List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        iterateResultSet(null, query, objectList, new RecordProcessor<ResultSet>() {

            // @Override
            public void process(int index, ResultSet record) throws Exception {
                Map<String, Object> map = new HashMap<String, Object>();
                ResultSetMetaData metaData = record.getMetaData();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    map.put(metaData.getColumnName(i), record.getObject(i));
                }
                mapList.add(map);
            }
        });
        return mapList;
    }

    public <T> void iterateObjects(final Class<T> clazz, String where, final List<Object> objectList,
                                   final RecordProcessor<T> recordProcessor) {
        iterateObjects(clazz, where, null, objectList, recordProcessor);
    }

    public <T> void iterateObjects(final Class<T> clazz, String where, String orderBy, final List<Object> objectList,
                                   final RecordProcessor<T> recordProcessor) {
        final String runSql = makeSQL(clazz, where, orderBy);
        execute(new Execute<List<T>>() {

            // @Override
            public List<T> execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Query sql: " + runSql);
                PreparedStatement preparedStatement = connection.prepareStatement(runSql);
                List<T> result = null;
                try {
                    if (objectList != null) {
                        for (int i = 0; i < objectList.size(); i++) {
                            int index = i + 1;
                            Object o = objectList.get(i);
                            if (logging && logTool.isInfoEnable()) logTool.info("Object @" + index + "=" + o);
                            Class<?> type = (o == null) ? null : o.getClass();
                            setPreparedStatmentByValue(preparedStatement, index, type, o);
                        }
                    }
                    result = new ArrayList<T>();
                    List<String> fieldList = DBUtil.getTableFields(clazz);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    try {
                        int index = 0;
                        while (resultSet.next()) {
                            T o = clazz.newInstance();
                            for (String f : fieldList) {
                                Field field = DBUtil.getClassFieldByDbName(clazz, f);
                                field.setAccessible(true);
                                setFiledByResultSet(resultSet, o, f, field);
                            }
                            recordProcessor.process(index, o);
                            index++;
                        }
                    } finally {
                        resultSet.close();
                    }
                } finally {
                    preparedStatement.close();
                }
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

            // @Override
            public List<?> execute(Connection connection) throws Exception {
                if (logging && logTool.isInfoEnable()) logTool.info("Query sql: " + runSql);
                PreparedStatement preparedStatement = connection.prepareStatement(runSql);
                List<?> result = null;
                try {
                    if (objectList != null) {
                        for (int i = 0; i < objectList.size(); i++) {
                            int index = i + 1;
                            Object o = objectList.get(i);
                            if (logging && logTool.isInfoEnable()) logTool.info("Object @" + index + "=" + o);
                            Class<?> type = (o == null) ? null : o.getClass();
                            setPreparedStatmentByValue(preparedStatement, index, type, o);
                        }
                    }
                    result = new ArrayList<Object>();
                    ResultSet resultSet = preparedStatement.executeQuery();
                    try {
                        int index = 0;
                        while (resultSet.next()) {
                            recordProcessor.process(index, resultSet);
                            index++;
                        }
                    } finally {
                        resultSet.close();
                    }
                } finally {
                    preparedStatement.close();
                }
                return result;
            }

        });
    }

    public static interface BatchExecute {

        void execute(DataAccessObject dao);
    }

    abstract public static class AbstractBatchExecute implements BatchExecute {

        private int count = 0;

        protected void updateConnection(DataAccessObject dao) {
            count++;
            if (count % 100 == 0) {
                try {
                    dao.getConnection().commit();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class WithBatchUpdateDataAccessObject extends DataAccessObject {

        private int commitCount = 100;

        public WithBatchUpdateDataAccessObject(ConnectionPool connectionPool) {
            super(connectionPool);
        }

        public void setCommitCount(int commitCount) {
            this.commitCount = commitCount;
        }

        protected <T> T execute(Execute<T> execute) {
            T result = super.execute(execute);

            int execCount = getConnectionExecCount();
            if ((execCount > 0) && (execCount % commitCount == 0)) {
                if (_connection != null) {
                    try {
                        _connection.commit();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            return result;
        }
    }

    public void batchExecute(BatchExecute batchExecute) {
        DataAccessObject _dao = this.borrowAsDataAccessObject();
        try {
            _dao.getConnection().setAutoCommit(false);

            batchExecute.execute(_dao);

            _dao.getConnection().commit();
            _dao.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            _dao.returnDataAccessObject();
        }
    }

    public void batchExecuteWithAutoCommit(BatchExecute batchExecute) {
        batchExecuteWithAutoCommit(batchExecute, 100);
    }

    public void batchExecuteWithAutoCommit(BatchExecute batchExecute, int commitCount) {
        WithBatchUpdateDataAccessObject _dao = this.borrowAsDataAccessObject(WithBatchUpdateDataAccessObject.class);
        try {
            _dao.getConnection().setAutoCommit(false);
            _dao.setCommitCount(commitCount);

            batchExecute.execute(_dao);

            _dao.getConnection().commit();
            _dao.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            _dao.returnDataAccessObject();
        }
    }

    protected <T> String makeCountSQL(Class<T> clazz, String where) {
        where = StringUtil.trim(where);
        if (where != null) {
            if (where.toUpperCase().startsWith("SELECT")) {
                return where;
            } else if (where.startsWith("!")) {
                return where.substring(1);
            }
        }
        String sql = "select count(*) count from " + DBUtil.getTableName(clazz);
        if ((where != null) && (where.length() > 0)) {
            sql += " where " + where;
        }
        return sql;
    }

    protected String makeSQL(Class<?> clazz, String where, String orderBy) {
        where = StringUtil.trim(where);
        String sql = null;
        if (where != null) {
            if (where.toUpperCase().startsWith("SELECT")) {
                sql = where;
            } else if (where.startsWith("!")) {
                sql = where.substring(1);
            }
        }
        if (sql == null) {
            sql = "select * from " + DBUtil.getTableName(clazz);
            if ((where != null) && (where.length() > 0)) {
                sql += " where " + where;
            }
        }
        if ((orderBy != null) && (orderBy.trim().length() > 0)) {
            sql += " order by " + orderBy;
        }
        return sql;
    }

    protected <T> T first(List<T> list) {
        if ((list == null) || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    protected void setPreparedStatmentValues(PreparedStatement preparedStatement, Class<?> clazz, Object object,
                                             List<String> refFieldList) throws Exception {
        for (int i = 0; i < refFieldList.size(); i++) {
            int index = i + 1;
            AtomicReference<Class<?>> refType = new AtomicReference<Class<?>>();
            Object o = ReflectUtil.getFieldValue(clazz, object,
                                                 DBUtil.getClassFieldByDbName(clazz, refFieldList.get(i)), refType);
            if (logging && logTool.isInfoEnable()) logTool.info("Object @" + index + "=" + o);
            setPreparedStatmentByValue(preparedStatement, index, refType.get(), o);
        }
    }

    protected void setFiledByResultSet(ResultSet resultSet, Object o, String f, Field field)
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
            java.sql.Timestamp t = resultSet.getTimestamp(f);
            field.set(o, (t == null) ? null : new Date(t.getTime()));
        } else if (field.getType() == BigDecimal.class) {
            field.set(o, resultSet.getBigDecimal(f));
        } else if (field.getType() == Boolean.class) {
            field.set(o, resultSet.getBoolean(f));
        } else {
            throw new RuntimeException("Unsupoorted type: " + field.getType());
        }
    }

    protected static void setPreparedStatmentByValue(PreparedStatement preparedStatement, int index, Class<?> type,
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
