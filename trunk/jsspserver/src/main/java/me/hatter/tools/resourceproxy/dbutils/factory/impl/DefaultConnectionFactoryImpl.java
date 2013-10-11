package me.hatter.tools.resourceproxy.dbutils.factory.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig.Driver;
import me.hatter.tools.resourceproxy.dbutils.conn.ConnectionValidity;
import me.hatter.tools.resourceproxy.dbutils.conn.TimeBasedValidConnection;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.DatabaseDialect;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.impl.MySQLDialect;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.impl.OracleDialect;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.impl.SQLiteDialect;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.dialect.impl.UnknowDatabaseDialect;
import me.hatter.tools.resourceproxy.dbutils.factory.ConnectionFactory;

public class DefaultConnectionFactoryImpl implements ConnectionFactory {

    private PropertyConfig  propertyConfig;
    private DatabaseDialect databaseDialect;

    public DefaultConnectionFactoryImpl(PropertyConfig propertyConfig) {
        this.propertyConfig = propertyConfig;
        try {
            Class.forName(propertyConfig.getDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (Arrays.asList(Driver.ORACLE).contains(propertyConfig.getDriver())) {
            databaseDialect = new OracleDialect();
        } else if (Arrays.asList(Driver.MYSQL).contains(propertyConfig.getDriver())) {
            databaseDialect = new MySQLDialect();
        } else if (Arrays.asList(Driver.SQLITE).contains(propertyConfig.getDriver())) {
            databaseDialect = new SQLiteDialect();
        } else {
            databaseDialect = new UnknowDatabaseDialect();
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

    public DatabaseDialect getDatabaseDialect() {
        return databaseDialect;
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
