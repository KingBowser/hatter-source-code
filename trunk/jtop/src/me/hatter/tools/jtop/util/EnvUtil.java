package me.hatter.tools.jtop.util;

public class EnvUtil {

    public static int getPort() {
        return getInt("port", 1127);
    }

    public static int getDumpCount() {
        return getInt("dumpcount", 1);
    }

    public static int getThreadTopN() {
        return getInt("threadtopn", 5);
    }

    public static int getStacktraceTopN() {
        return getInt("stacktracetopn", 8);
    }

    public static String getSize() {
        return getStr("size", "b");
    }
    
    public static boolean getColor() {
        String color = getStr("color", "off");
        return "on".equals(color.trim().toLowerCase());
    }

    public static String getStr(String key, String def) {
        String val = System.getProperty(key);
        return (val == null) ? def : val;
    }

    public static int getInt(String key, int def) {
        String val = System.getProperty(key);
        return (val == null) ? def : Integer.parseInt(val);
    }
}
