package me.hatter.tools.jprop.management;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.management.ObjectName;
import javax.management.StandardMBean;

public class PropertyImpl extends StandardMBean implements PropertyMXBean {

    private static PropertyImpl PROPERTYIMPL_MXBEAN = null;

    public PropertyImpl() {
        super(PropertyMXBean.class, true);
    }

    synchronized public static void registerMXBean() {
        if (PROPERTYIMPL_MXBEAN != null) {
            return;
        }
        try {
            PROPERTYIMPL_MXBEAN = new PropertyImpl();
            ManagementFactory.getPlatformMBeanServer().registerMBean(PROPERTYIMPL_MXBEAN,
                                                                     new ObjectName(SYSTEM_PROPERTY_MXBEAN_NAME));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getPropertyMap() {
        Map<String, String> pmap = new LinkedHashMap<String, String>();
        Properties properties = System.getProperties();
        for (String k : properties.stringPropertyNames()) {
            pmap.put(k, properties.getProperty(k));
        }
        return pmap;
    }

    public String getProperty(String key) {
        return System.getProperty(key);
    }

    public void setPropery(String key, String value) {
        System.setProperty(key, value);
    }

    public void clearPropery(String key) {
        System.getProperties().remove(key);
    }

}
