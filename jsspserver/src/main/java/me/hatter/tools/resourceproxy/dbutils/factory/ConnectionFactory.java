package me.hatter.tools.resourceproxy.dbutils.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.conn.ConnectionValidity;
import me.hatter.tools.resourceproxy.dbutils.conn.TimeBasedValidConnection;

public class ConnectionFactory {

    private PropertyConfig propertyConfig;

    public ConnectionFactory(PropertyConfig propertyConfig) {
        this.propertyConfig = propertyConfig;
        try {
            Class.forName(propertyConfig.getDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(propertyConfig.getJdbcUrl(),
                                                                propertyConfig.getUserName(),
                                                                propertyConfig.getPassword());
            return new TimeBasedValidConnection(connection, TimeUnit.MINUTES.toMillis(3));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validConnection(Connection connection) {
        if (connection == null) {
            return false;
        }
        if (connection instanceof ConnectionValidity) {
            return ((ConnectionValidity) connection).isConnValid();
        }
        return true;
    }

    public void destoryConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
