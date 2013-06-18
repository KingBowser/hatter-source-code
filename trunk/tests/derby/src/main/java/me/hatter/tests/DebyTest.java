package me.hatter.tests;

import java.util.Properties;

import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;

// String driver = "org.apache.derby.jdbc.EmbeddedDriver";
// String dbName="jdbcDemoDB";
// String connectionURL = "jdbc:derby:" + dbName + ";create=true";
//
// String driver = "org.apache.derby.jdbc.ClientDriver";
// ...
// String connectionURL = "jdbc:derby://localhost:1527/" + dbName + ";create=true";

// ij
// CONNECT 'jdbc:derby:/Users/hatterjiang/Code/hatter-source-code/tests/derby/derby_test.db';
// help;
public class DebyTest {

    static final String DRIVER  = "org.apache.derby.jdbc.ClientDriver";
    static final String DRIVER2 = "org.apache.derby.jdbc.EmbeddedDriver";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("jdbc.driver", DRIVER2);
        properties.setProperty("jdbc.url", "jdbc:derby:"
                                           + "/Users/hatterjiang/Code/hatter-source-code/tests/derby/derby_test.db");
        DataAccessObject _dao = new DataAccessObject(PropertyConfig.createPropertyConfig(properties));

        _dao.executeSql("create table test(a int, b varchar(40))", null);
    }
}
