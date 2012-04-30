package me.hatter.tools.commons.log;

public class LogUtil {

    public static enum LogType {
        INFO,

        WARN,

        ERROR
    }

    public static void info(String message) {
        message(LogType.INFO, message);
    }

    public static void warn(String message) {
        message(LogType.WARN, message);
    }

    public static void error(String message) {
        message(LogType.ERROR, message);
    }

    public static void message(LogType type, String message) {
        StringBuilder msg = new StringBuilder();
        if (type != null) {
            msg.append("[");
            msg.append(type.name());
            msg.append("] ");
        }
        msg.append(message);
        System.out.println(msg.toString());
    }
}
