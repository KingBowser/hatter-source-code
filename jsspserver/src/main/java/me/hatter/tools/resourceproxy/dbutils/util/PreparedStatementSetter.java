package me.hatter.tools.resourceproxy.dbutils.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

// @Clazz(ABC.class)
public interface PreparedStatementSetter {

    public void set(PreparedStatement statement, int index, Object value) throws SQLException;
}
