package me.hatter.tools.commons.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ResultSetTool {

    private ResultSet resultSet;

    public ResultSetTool(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public <T> T getValue(Class<T> clazz, String columnLabel) throws SQLException {
        return getValue(clazz, resultSet.findColumn(columnLabel));
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(Class<T> clazz, int index) throws SQLException {
        if (clazz == String.class) {
            return (T) resultSet.getString(index);
        }
        if ((clazz == boolean.class) || (clazz == Boolean.class)) {
            return (T) ((Boolean) resultSet.getBoolean(index));
        }
        if ((clazz == byte.class) || (clazz == Byte.class)) {
            return (T) ((Byte) resultSet.getByte(index));
        }
        if ((clazz == short.class) || (clazz == Short.class)) {
            return (T) ((Short) resultSet.getShort(index));
        }
        if ((clazz == int.class) || (clazz == Integer.class)) {
            return (T) ((Integer) resultSet.getInt(index));
        }
        if ((clazz == long.class) || (clazz == Long.class)) {
            return (T) ((Long) resultSet.getLong(index));
        }
        if ((clazz == float.class) || (clazz == Float.class)) {
            return (T) ((Float) resultSet.getFloat(index));
        }
        if ((clazz == double.class) || (clazz == Double.class)) {
            return (T) ((Double) resultSet.getDouble(index));
        }
        if (clazz == Date.class) {
            java.sql.Date _date = resultSet.getDate(index);
            if (_date == null) {
                return null;
            }
            return (T) new Date(_date.getTime());
        }

        throw new IllegalArgumentException("Unsupported type: " + clazz);
    }
}
