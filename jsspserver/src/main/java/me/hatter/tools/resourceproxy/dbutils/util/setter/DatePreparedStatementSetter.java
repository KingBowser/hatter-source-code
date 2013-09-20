package me.hatter.tools.resourceproxy.dbutils.util.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import me.hatter.tools.resourceproxy.dbutils.annotation.Clazz;
import me.hatter.tools.resourceproxy.dbutils.util.PreparedStatementSetter;

@Clazz(Date.class)
public class DatePreparedStatementSetter implements PreparedStatementSetter {

    // @Override
    public void set(PreparedStatement statement, int index, Object value) throws SQLException {
        statement.setDate(index, new java.sql.Date(((Date) value).getTime()));
    }
}
