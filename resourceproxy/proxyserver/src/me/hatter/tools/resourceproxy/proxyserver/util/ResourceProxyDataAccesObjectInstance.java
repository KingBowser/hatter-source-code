package me.hatter.tools.resourceproxy.proxyserver.util;

import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;

public class ResourceProxyDataAccesObjectInstance {

    public static final DataAccessObject DATA_ACCESS_OBJECT = new DataAccessObject(
                                                                                   PropertyConfig.createDefaultPropertyConfig());
}
