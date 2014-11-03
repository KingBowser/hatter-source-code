package me.hatter.tools.jprop.management;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.management.ObjectName;
import javax.management.StandardMBean;

import sun.misc.VMSupport;

public class AgentPropertyImpl extends StandardMBean implements PropertyMXBean {

    private static AgentPropertyImpl PROPERTYIMPL_MXBEAN = null;

    public AgentPropertyImpl() {
        super(PropertyMXBean.class, true);
    }

    synchronized public static void registerMXBean() {
        if (PROPERTYIMPL_MXBEAN != null) {
            return;
        }
        try {
            PROPERTYIMPL_MXBEAN = new AgentPropertyImpl();
            ManagementFactory.getPlatformMBeanServer().registerMBean(PROPERTYIMPL_MXBEAN,
                                                                     new ObjectName(AGENT_PROPERTY_MXBEAN_NAME));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getPropertyMap() {
        Map<String, String> pmap = new LinkedHashMap<String, String>();
        Properties properties = VMSupport.getAgentProperties();
        for (String k : properties.stringPropertyNames()) {
            pmap.put(k, properties.getProperty(k));
        }
        return pmap;
    }

    public String getProperty(String key) {
        return VMSupport.getAgentProperties().getProperty(key);
    }

    public void setPropery(String key, String value) {
        VMSupport.getAgentProperties().setProperty(key, value);
    }

    public void clearPropery(String key) {
        VMSupport.getAgentProperties().remove(key);
    }
}
