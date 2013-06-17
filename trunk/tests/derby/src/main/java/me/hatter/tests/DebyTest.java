package me.hatter.tests;

import java.util.Properties;

import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;

public class DebyTest {

    static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";

    public static void main(String[] args) {
        Properties properties = new Properties();
        DataAccessObject _dao = new DataAccessObject(PropertyConfig.createPropertyConfig(properties));

    }
}
