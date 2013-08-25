package me.hatter.tools.jsspserver.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;

public abstract class DatabaseAction extends BaseAction {

    public static final DataAccessObject DAO;
    public static final Properties       APP_PROPERTIES;
    static {
        String dbconfigdir = Environment.getStrProperty("dbconfigdir", Environment.USER_DIR);
        DAO = new DataAccessObject(PropertyConfig.createPropertyConfig(new File(dbconfigdir, "dbconfig.xml")));
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        File appPropertiesFile = new File(dbconfigdir, "appconfig.xml");
        Properties appProperties = new Properties();
        if (appPropertiesFile.exists()) {
            InputStream is;
            try {
                is = new FileInputStream(appPropertiesFile);
                appProperties.loadFromXML(is);
            } catch (IOException e) {
                LogUtil.error("Load app properties failed: " + appPropertiesFile, e);
            }
        } else {
            LogUtil.info("App properties file not exists: " + appPropertiesFile);
        }
        APP_PROPERTIES = appProperties;
    }
}
