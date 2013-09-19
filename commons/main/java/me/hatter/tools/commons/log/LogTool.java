package me.hatter.tools.commons.log;

public interface LogTool {

    boolean isTraceEnable();

    boolean isDebugEnable();

    boolean isInfoEnable();

    boolean isWarnEnable();

    void trace(String message);

    void trace(String message, Throwable t);

    void debug(String message);

    void debug(String message, Throwable t);

    void info(String message);

    void info(String message, Throwable t);

    void warn(String message);

    void warn(String message, Throwable t);

    void error(String message);

    void error(String message, Throwable t);
}
