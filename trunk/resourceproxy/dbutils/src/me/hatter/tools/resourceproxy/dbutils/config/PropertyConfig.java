package me.hatter.tools.resourceproxy.dbutils.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyConfig {

    public static interface Driver {

        String SQLITE = "org.sqlite.JDBC";
    }

    private static final String CONFIG_FILE = "config.xml";

    private Properties          properties  = new Properties();

    private PropertyConfig() {
    }

    public static PropertyConfig createDefaultPropertyConfig() {
        return createPropertyConfig(new File(System.getProperty("user.dir"), CONFIG_FILE));
    }

    public static PropertyConfig createPropertyConfig(File f) {
        PropertyConfig propertyConfig = new PropertyConfig();
        InputStream is = null;
        try {
            is = new FileInputStream(f);
            propertyConfig.properties.loadFromXML(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) { // IGNORE
                }
            }
        }
        return propertyConfig;
    }

    public static PropertyConfig createPropertyConfig(Properties properties) {
        PropertyConfig propertyConfig = new PropertyConfig();
        propertyConfig.properties.putAll(properties);
        return propertyConfig;
    }

    public static PropertyConfig createSqliteConfig(String sqliteFile) {
        Properties properties = new Properties();
        properties.setProperty("jdbc.driver", Driver.SQLITE);
        properties.setProperty("jdbc.url", "jdbc:sqlite:" + sqliteFile);
        return createPropertyConfig(properties);
    }

    /**
     * Oracle: oracle.jdbc.OracleDriver<br>
     * SQLite: org.sqlite.JDBC
     */
    public String getDriver() {
        return properties.getProperty("jdbc.driver");
    }

    public String getJdbcUrl() {
        return properties.getProperty("jdbc.url");
    }

    public String getUserName() {
        return properties.getProperty("jdbc.username");
    }

    public String getPassword() {
        return properties.getProperty("jdbc.password");
    }
}
