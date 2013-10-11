package me.hatter.tools.resourceproxy.dbutils.factory;

import java.sql.Connection;

public interface ConnectionPool {

    ConnectionFactory getConnectionFactory();

    Connection borrowConnection();

    void returnConnection(Connection connection);

    void returnConnectionWithError(Connection connection);

    void returnConnectionWithError(Connection connection, Throwable throwable);
}
