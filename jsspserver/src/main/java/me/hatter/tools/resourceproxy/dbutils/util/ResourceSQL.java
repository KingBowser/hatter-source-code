package me.hatter.tools.resourceproxy.dbutils.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.io.IOUtil;

public class ResourceSQL {

    private String                      resource;
    private Properties                  resourceProperties = new Properties();
    private ConcurrentMap<String, XSQL> sqlCacheMap        = new ConcurrentHashMap<String, XSQL>();

    public ResourceSQL(String resource) {
        this(ClassLoaderUtil.getClassLoaderByClass(ResourceSQL.class), resource);
    }

    public ResourceSQL(ClassLoader classLoader, String resource) {
        this.resource = resource;
        InputStream is = classLoader.getResourceAsStream(this.resource);
        try {
            resourceProperties.loadFromXML(is);
        } catch (Exception e) {
            IOUtil.closeQuitely(is);
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    public XSQL sql(String id) {
        XSQL xsql = sqlCacheMap.get(id);
        if (xsql != null) {
            return xsql;
        }
        xsql = new XSQL(id, resourceProperties);
        sqlCacheMap.putIfAbsent(id, xsql);
        return xsql;
    }
}
