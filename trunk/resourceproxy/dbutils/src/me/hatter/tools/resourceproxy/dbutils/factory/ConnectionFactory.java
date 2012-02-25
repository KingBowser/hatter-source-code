package me.hatter.tools.resourceproxy.dbutils.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;

public class ConnectionFactory {

    static {
        try {
            Class.forName(PropertyConfig.getDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(PropertyConfig.getJdbcUrl(),
                                                                PropertyConfig.getUserName(),
                                                                PropertyConfig.getPassword());
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destoryConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
