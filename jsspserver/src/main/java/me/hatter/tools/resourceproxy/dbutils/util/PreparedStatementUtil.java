package me.hatter.tools.resourceproxy.dbutils.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import me.hatter.tools.resourceproxy.dbutils.annotation.Clazz;

public class PreparedStatementUtil {

    public static class DefaultObjectPreparedStatementSetter implements PreparedStatementSetter {

        // @Override
        public void set(PreparedStatement statement, int index, Object value) throws SQLException {
            statement.setObject(index, value);
        }
    }

    public static final PreparedStatementSetter           DEFAULT_OBJECT_PREPARED_STATEMENT_SETTER = new DefaultObjectPreparedStatementSetter();
    private static Map<Class<?>, PreparedStatementSetter> CLASS_PREPARED_STATEMENT_SETTER_MAP      = new HashMap<Class<?>, PreparedStatementSetter>();

    static {
        ServiceLoader<PreparedStatementSetter> preparedStatementSetterLoader = ServiceLoader.load(PreparedStatementSetter.class);
        for (PreparedStatementSetter preparedStatementSetter : preparedStatementSetterLoader) {
            Clazz clazz = preparedStatementSetter.getClass().getAnnotation(Clazz.class);
            if (clazz == null) {
                throw new RuntimeException("Clazz annotation is no present:  " + preparedStatementSetter.getClass());
            }
            CLASS_PREPARED_STATEMENT_SETTER_MAP.put(clazz.value(), preparedStatementSetter);
        }
    }

    public static PreparedStatementSetter getPreparedStatementSetter(Class<?> clazz) {
        if (clazz == null) {
            return DEFAULT_OBJECT_PREPARED_STATEMENT_SETTER;
        }
        PreparedStatementSetter preparedStatementSetter = CLASS_PREPARED_STATEMENT_SETTER_MAP.get(clazz);
        if (preparedStatementSetter == null) {
            throw new RuntimeException("Cannot find prepared statement setter for: " + clazz);
        }
        return preparedStatementSetter;
    }
}
