package me.hatter.tools.jprop.management;

import java.util.Map;

public interface PropertyMXBean {

    public static final String SYSTEM_PROPERTY_MXBEAN_NAME = "me.hatter.management:type=Property,name=system";
    public static final String AGENT_PROPERTY_MXBEAN_NAME  = "me.hatter.management:type=Property,name=agent";

    Map<String, String> getPropertyMap();

    String getProperty(String key);

    void setPropery(String key, String value);

    void clearPropery(String key);
}
