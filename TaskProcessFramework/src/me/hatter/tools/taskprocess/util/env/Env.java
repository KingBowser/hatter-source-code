package me.hatter.tools.taskprocess.util.env;

import java.io.File;
import java.util.Arrays;

public class Env {

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String UTF_8    = "UTF-8";

    public static File newUserDirFile(String filename) {
        return new File(Env.USER_DIR, filename);
    }

    public static int getIntProperty(String property, int defaultValue) {
        return Integer.parseInt(getProperty(property, String.valueOf(defaultValue)));
    }

    public static long getLongProperty(String property, long defaultValue) {
        return Long.parseLong(getProperty(property, String.valueOf(defaultValue)));
    }

    public static boolean getBoolProperty(String property, boolean defaultValue) {
        return isOn(getProperty(property, String.valueOf(defaultValue)));
    }

    public static String getProperty(String property, String defaultValue) {
        String value = System.getProperty(property, defaultValue);
        System.out.println("[INFO] Property '" + property + "' is set to: " + value);
        return value;
    }

    public static boolean getBoolPropertyOrDie(String property) {
        String v = getPropertyOrDie(property);
        return isOn(v);
    }

    public static String getPropertyOrDie(String property) {
        String value = System.getProperty(property);
        if (value == null) {
            System.out.println("[ERROR] Property '" + property + "' is null.");
            System.exit(0);
        }
        System.out.println("[INFO] Property '" + property + "' is set to: " + value);
        return value;
    }

    public static boolean isOn(String v) {
        if (v == null) {
            return false;
        }
        return Arrays.asList("Y", "ON", "OK", "YES", "TRUE").contains(v.toUpperCase());
    }
}
