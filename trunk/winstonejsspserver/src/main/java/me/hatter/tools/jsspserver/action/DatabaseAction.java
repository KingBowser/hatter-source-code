package me.hatter.tools.jsspserver.action;

import java.io.File;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;

public abstract class DatabaseAction extends BaseAction {

    protected static final DataAccessObject DAO;
    static {
        String dbconfigdir = Environment.getStrProperty("dbconfigdir", Environment.USER_DIR);
        DAO = new DataAccessObject(PropertyConfig.createPropertyConfig(new File(dbconfigdir, "dbconfig.xml")));
    }
}
