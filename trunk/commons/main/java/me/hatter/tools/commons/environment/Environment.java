package me.hatter.tools.commons.environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;

public class Environment {

    private static final LogTool logTool           = LogTools.getLogTool(Environment.class);

    public static final String   DEFAULT_SEPARATER = ",";

    public static final String   OS_NAME           = System.getProperty("os.name");
    public static final String   OS_ARCH           = System.getProperty("os.arch");
    public static final String   OS_VERSION        = System.getProperty("os.version");
    public static final String   JAVA_VENDOR       = System.getProperty("java.vendor");

    public static final String   JAVA_HOME         = System.getProperty("java.home");
    public static final String   USER_DIR          = System.getProperty("user.dir");
    public static final String   USER_NAME         = System.getProperty("user.name");
    public static final String   USER_HOME         = System.getProperty("user.home");
    public static final String   JDK_HOME          = Environment.JAVA_HOME.replaceAll("\\/jre(\\/)?$", "");

    // File separator ("/" on UNIX)
    public static final String   FILE_SEPARATOR    = System.getProperty("file.separator");
    // Path separator (":" on UNIX)
    public static final String   PATH_SEPARATOR    = System.getProperty("path.separator");
    // Line separator ("\n" on UNIX)
    public static final String   LINE_SEPARATOR    = System.getProperty("line.separator");

    public static enum JavaVendor {
        Apple("Apple"),

        Sun("Sun"),

        Oracle("Oracle"),

        Unknow(null);

        private String vendor;

        private JavaVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getVendor() {
            return this.vendor;
        }
    }

    public static File getUserDesktop() {
        if (getVendor() == JavaVendor.Apple) {
            File desktop = new File(USER_HOME, "Desktop");
            if (desktop.exists() && desktop.isDirectory()) {
                return desktop;
            }
        }
        return new File(USER_HOME); // XXX
    }

    public static JavaVendor getVendor() {
        String jv = JAVA_VENDOR.trim();
        if (jv.startsWith(JavaVendor.Apple.getVendor())) {
            return JavaVendor.Apple;
        }
        if (jv.startsWith(JavaVendor.Sun.getVendor())) {
            return JavaVendor.Sun;
        }
        if (jv.startsWith(JavaVendor.Oracle.getVendor())) {
            return JavaVendor.Oracle;
        }
        return JavaVendor.Unknow;
    }

    public static String getStrPropertyOrDie(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            logTool.error("Property not exists: " + key);
            System.exit(-1);
        }
        logTool.trace("Property " + key + " is: " + value);
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

    public static List<String> getStrPropertyList(String key, List<String> defaultValue) {
        return getStrPropertyList(key, DEFAULT_SEPARATER, defaultValue);
    }

    public static List<String> getStrPropertyList(String key, String regexSeparater, List<String> defaultValue) {
        List<String> result = getInnerStrPropertyList(key, regexSeparater);
        result = (result == null) ? defaultValue : result;
        logTool.trace("Property list " + key + " is: " + result);
        return result;
    }

    public static List<Integer> getIntPropertyList(String key, List<Integer> defaultValue) {
        return getIntPropertyList(key, DEFAULT_SEPARATER, defaultValue);
    }

    public static List<Integer> getIntPropertyList(String key, String regexSeparater, List<Integer> defaultValue) {
        List<String> result = getInnerStrPropertyList(key, regexSeparater);
        List<Integer> intListResult = new ArrayList<Integer>();
        if (result != null) {
            for (String v : result) {
                try {
                    Integer i = Integer.valueOf(v);
                    intListResult.add(i);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot parse property key/value: " + key + " / " + v + " of: " + result);
                }
            }
        }
        intListResult = intListResult.isEmpty() ? defaultValue : intListResult;
        logTool.trace("Property list " + key + " is: " + intListResult);
        return intListResult;
    }

    public static List<Long> getLongPropertyList(String key, List<Long> defaultValue) {
        return getLongPropertyList(key, DEFAULT_SEPARATER, defaultValue);
    }

    public static List<Long> getLongPropertyList(String key, String regexSeparater, List<Long> defaultValue) {
        List<String> result = getInnerStrPropertyList(key, regexSeparater);
        List<Long> longListResult = new ArrayList<Long>();
        if (result != null) {
            for (String v : result) {
                try {
                    Long l = Long.valueOf(v);
                    longListResult.add(l);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot parse property key/value: " + key + " / " + v + " of: " + result);
                }
            }
        }
        longListResult = longListResult.isEmpty() ? defaultValue : longListResult;
        logTool.trace("Property list " + key + " is: " + longListResult);
        return longListResult;
    }

    public static List<Double> getDoublePropertyList(String key, List<Double> defaultValue) {
        return getDoublePropertyList(key, DEFAULT_SEPARATER, defaultValue);
    }

    public static List<Double> getDoublePropertyList(String key, String regexSeparater, List<Double> defaultValue) {
        List<String> result = getInnerStrPropertyList(key, regexSeparater);
        List<Double> doubleListResult = new ArrayList<Double>();
        if (result != null) {
            for (String v : result) {
                try {
                    Double d = Double.valueOf(v);
                    doubleListResult.add(d);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot parse property key/value: " + key + " / " + v + " of: " + result);
                }
            }
        }
        doubleListResult = doubleListResult.isEmpty() ? defaultValue : doubleListResult;
        logTool.trace("Property list " + key + " is: " + doubleListResult);
        return doubleListResult;
    }

    public static String getStrProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        String s = (value == null) ? defaultValue : value;
        logTool.trace("Property " + key + " is: " + s);
        return s;
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = System.getProperty(key);
        int i = (value == null) ? defaultValue : Integer.parseInt(value);
        logTool.trace("Property " + key + " is: " + i);
        return i;
    }

    public static long getLongProperty(String key, long defaultValue) {
        String value = System.getProperty(key);
        long l = (value == null) ? defaultValue : Long.parseLong(value);
        logTool.trace("Property " + key + " is: " + l);
        return l;
    }

    public static double getDoubleProperty(String key, double defaultValue) {
        String value = System.getProperty(key);
        double d = (value == null) ? defaultValue : Double.parseDouble(value);
        logTool.trace("Property " + key + " is: " + d);
        return d;
    }

    public static boolean getBoolProperty(String key, boolean defaultValue) {
        String value = System.getProperty(key);
        boolean b = (value == null) ? defaultValue : Boolean.parseBoolean(value);
        logTool.trace("Property " + key + " is: " + b);
        return b;
    }

    private static List<String> getInnerStrPropertyList(String key, String regexSeparater) {
        String value = System.getProperty(key);
        if (value == null) {
            return null;
        }
        String[] vals = value.split(regexSeparater);
        List<String> result = new ArrayList<String>();
        for (String v : vals) {
            v = v.trim();
            if (!v.isEmpty()) {
                result.add(v);
            }
        }
        return result.isEmpty() ? null : result;
    }
}
