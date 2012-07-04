package me.hatter.tools.jtop.util;

public class EnvUtil {

    public static String getPid() {
        return getStr("pid", null);
    }

    public static int getPort() {
        return getInt("port", 1127);
    }

    public static int getDumpCount() {
        return getInt("count", 1);
    }

    public static int getThreadTopN() {
        return getInt("thread", 5);
    }

    public static int getStacktraceTopN() {
        return getInt("stack", 8);
    }

    public static String getSize() {
        return getStr("size", "b");
    }

    public static long getSleepMillis() {
        return getLong("sleep", 2000L);
    }

    public static boolean getColor() {
        return UnixArgsutil.ARGS.flags().contains("color");
    }

    public static String getStr(String key, String def) {
        String val = UnixArgsutil.ARGS.kvalue(key);
        return (val == null) ? def : val;
    }

    public static long getLong(String key, long def) {
        String val = UnixArgsutil.ARGS.kvalue(key);
        return (val == null) ? def : Long.parseLong(val);
    }

    public static int getInt(String key, int def) {
        String val = UnixArgsutil.ARGS.kvalue(key);
        return (val == null) ? def : Integer.parseInt(val);
    }
}
