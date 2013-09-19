package me.hatter.tools.commons.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.hatter.tools.commons.exception.ExceptionUtil;

public class LogUtil {

    public static enum LogType {

        TRACE,

        DEBUG,

        INFO,

        WARN,

        ERROR
    }

    private static Set<LogType> LOG_TYPE_SET = new HashSet<LogUtil.LogType>();
    static {
        String logTypes = System.getProperty("log.types");
        List<LogType> logTypeList = new ArrayList<LogType>();
        if (logTypes != null) {
            String[] logTypesArray = logTypes.split("[,;]");
            for (String lt : logTypesArray) {
                for (LogType lte : LogType.values()) {
                    if (lte.name().equalsIgnoreCase(lt.trim())) {
                        logTypeList.add(lte);
                    }
                }
            }
        }
        setLogTypeSet(((logTypeList == null) || logTypeList.isEmpty()) ? Arrays.asList(LogType.values()) : logTypeList,
                      (logTypes != null));
    }

    synchronized public static void setLogTypeSet(Collection<LogType> logTypeSet, boolean printSetInfo) {
        Set<LogType> logTypes = new HashSet<LogUtil.LogType>();
        logTypes.add(LogType.ERROR);
        if (logTypeSet != null) {
            logTypes.addAll(logTypeSet);
        }
        if (printSetInfo) {
            System.out.println("[INFO] Log types is set to: " + logTypes);
        }
        LOG_TYPE_SET = logTypes;
    }

    synchronized public static boolean isTraceEnable() {
        return LOG_TYPE_SET.contains(LogType.TRACE);
    }

    synchronized public static boolean isDebugEnable() {
        return LOG_TYPE_SET.contains(LogType.DEBUG);
    }

    synchronized public static boolean isInfoEnable() {
        return LOG_TYPE_SET.contains(LogType.INFO);
    }

    synchronized public static boolean isWarnEnable() {
        return LOG_TYPE_SET.contains(LogType.WARN);
    }

    synchronized public static void trace(String message) {
        message(LogType.TRACE, message);
    }

    synchronized public static void trace(String message, Throwable t) {
        message(LogType.TRACE, message + ", exception: " + ExceptionUtil.printStackTrace(t));
    }

    synchronized public static void debug(String message) {
        message(LogType.DEBUG, message);
    }

    synchronized public static void debug(String message, Throwable t) {
        message(LogType.DEBUG, message + ", exception: " + ExceptionUtil.printStackTrace(t));
    }

    synchronized public static void info(String message) {
        message(LogType.INFO, message);
    }

    synchronized public static void info(String message, Throwable t) {
        message(LogType.INFO, message + ", exception: " + ExceptionUtil.printStackTrace(t));
    }

    synchronized public static void warn(String message) {
        message(LogType.WARN, message);
    }

    synchronized public static void warn(String message, Throwable t) {
        message(LogType.WARN, message + ", exception: " + ExceptionUtil.printStackTrace(t));
    }

    synchronized public static void error(String message) {
        message(LogType.ERROR, message);
    }

    synchronized public static void error(String message, Throwable t) {
        message(LogType.ERROR, message + ", exception: " + ExceptionUtil.printStackTrace(t));
    }

    synchronized public static void message(LogType type, String message) {
        if (type != null) {
            if (!LOG_TYPE_SET.contains(type)) {
                return;
            }
        }
        StringBuilder msg = new StringBuilder((message == null) ? 16 : (message.length() + 10));
        if (type != null) {
            msg.append("[");
            msg.append(type.name());
            msg.append("] ");
        }
        msg.append(message);
        System.out.println(msg.toString());
    }
}
