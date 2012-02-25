package me.hatter.tools.resourceproxy.dbutils.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyConfig {

    private static final String CONFIG_FILE = "config.xml";

    private static Properties   properties  = new Properties();

    static {
        InputStream is = null;
        try {
            is = new FileInputStream(new File(System.getProperty("user.dir"), CONFIG_FILE));
            properties.loadFromXML(is);
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
    }

    /**
     * Oracle: oracle.jdbc.OracleDriver<br>
     * SQLite: org.sqlite.JDBC
     */
    public static String getDriver() {
        return properties.getProperty("jdbc.driver");
    }

    public static String getJdbcUrl() {
        return properties.getProperty("jdbc.url");
    }

    public static String getUserName() {
        return properties.getProperty("jdbc.username");
    }

    public static String getPassword() {
        return properties.getProperty("jdbc.password");
    }
}
