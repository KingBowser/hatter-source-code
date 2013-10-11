package me.hatter.tools.resourceproxy.dbutils.factory;

import java.sql.Connection;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.DatabaseDialect;

public interface ConnectionFactory {

    DatabaseDialect getDatabaseDialect();

    Connection getConnection();

    boolean validConnection(Connection connection);

    void destoryConnection(Connection connection);
}
