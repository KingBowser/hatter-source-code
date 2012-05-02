package me.hatter.tools.commons.environment;

import me.hatter.tools.commons.log.LogUtil;

public class Environment {

    public static final String USER_DIR  = System.getProperty("user.dir");
    public static final String USER_HOME = System.getProperty("user.home");

    public static String getStrPropertyOrDie(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            LogUtil.error("Property not exists: " + key);
            System.exit(-1);
        }
        LogUtil.trace("Property " + key + " is: " + value);
        return value;
    }

    public static int getIntPropertyOrDie(String key) {
        return Integer.parseInt(getStrPropertyOrDie(key));
    }

    public long getLongPropertyOrDie(String key) {
        return Long.parseLong(getStrPropertyOrDie(key));
    }

    public double getDoublePropertyOrDie(String key) {
        return Double.parseDouble(getStrPropertyOrDie(key));
    }

    public boolean getBoolPropertyOrDie(String key) {
        return Boolean.parseBoolean(getStrPropertyOrDie(key));
    }

    public static String getStrProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        String s = (value == null) ? defaultValue : value;
        LogUtil.trace("Property " + key + " is: " + s);
        return s;
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = System.getProperty(key);
        int i = (value == null) ? defaultValue : Integer.parseInt(value);
        LogUtil.trace("Property " + key + " is: " + i);
        return i;
    }

    public static long getLongProperty(String key, long defaultValue) {
        String value = System.getProperty(key);
        long l = (value == null) ? defaultValue : Long.parseLong(value);
        LogUtil.trace("Property " + key + " is: " + l);
        return l;
    }

    public static double getDoubleProperty(String key, double defaultValue) {
        String value = System.getProperty(key);
        double d = (value == null) ? defaultValue : Double.parseDouble(value);
        LogUtil.trace("Property " + key + " is: " + d);
        return d;
    }

    public static boolean getBoolProperty(String key, boolean defaultValue) {
        String value = System.getProperty(key);
        boolean b = (value == null) ? defaultValue : Boolean.parseBoolean(value);
        LogUtil.trace("Property " + key + " is: " + b);
        return b;
    }
}
