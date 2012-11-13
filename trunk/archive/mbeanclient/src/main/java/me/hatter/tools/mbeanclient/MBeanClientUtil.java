package me.hatter.tools.mbeanclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jmx.RemoteManagementTool;
import me.hatter.tools.commons.log.LogUtil;

public class MBeanClientUtil {

    public static final String                   pid             = UnixArgsutil.ARGS.args()[0];
    public static final RemoteManagementTool     tool            = new RemoteManagementTool(pid);
    public static final MBeanServerConnection    connection;
    static {
        try {
            connection = tool.getJmxConnector().getMBeanServerConnection();
        } catch (IOException e) {
            LogUtil.error("Get mbean server connection error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static final Map<Integer, ObjectName> idObjectNameMap = new HashMap<Integer, ObjectName>();

    public static Set<ObjectName> queryMBeanNames() {
        Set<ObjectName> mbeanNames;
        try {
            mbeanNames = connection.queryNames(null, null);
        } catch (IOException e) {
            LogUtil.error("Query mbean error: " + e.getMessage(), e);
            return null;
        }
        return mbeanNames;
    }
}
