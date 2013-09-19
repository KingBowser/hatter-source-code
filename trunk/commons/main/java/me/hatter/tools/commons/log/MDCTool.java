package me.hatter.tools.commons.log;

import java.util.Map;

public interface MDCTool {

    public void put(String key, String val);

    public String get(String key);

    public void remove(String key);

    public void clear();

    public Map<String, String> getCopyOfContextMap();

    public void setContextMap(Map<String, String> contextMap);
}
