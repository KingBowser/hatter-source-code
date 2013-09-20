package me.hatter.tools.commons.log.impl;

import java.util.Map;

import me.hatter.tools.commons.log.MDCTool;

import org.apache.log4j.MDC;

public class Log4jMDCTool implements MDCTool {

    public void put(String key, String val) {
        MDC.put(key, val);
    }

    public String get(String key) {
        return (String) MDC.get(key);
    }

    public void remove(String key) {
        MDC.remove(key);
    }

    public void clear() {
        MDC.getContext().clear();
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getCopyOfContextMap() {
        return MDC.getContext();
    }

    public void setContextMap(Map<String, String> contextMap) {
        throw new IllegalArgumentException("Lo4jMDCTool cannot set context map!");
    }
}
