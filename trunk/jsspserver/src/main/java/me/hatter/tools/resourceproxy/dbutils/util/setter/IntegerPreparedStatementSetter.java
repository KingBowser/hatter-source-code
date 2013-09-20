package me.hatter.tools.resourceproxy.dbutils.util.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import me.hatter.tools.resourceproxy.dbutils.annotation.Clazz;
import me.hatter.tools.resourceproxy.dbutils.util.PreparedStatementSetter;

@Clazz(Integer.class)
public class IntegerPreparedStatementSetter implements PreparedStatementSetter {

    // @Override
    public void set(PreparedStatement statement, int index, Object value) throws SQLException {
        statement.setInt(index, (Integer) value);
    }
}
