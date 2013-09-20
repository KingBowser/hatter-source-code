package me.hatter.tools.commons.log.impl;

import me.hatter.tools.commons.log.LogTool;

import org.apache.log4j.Logger;

public class Log4jLogTool implements LogTool {

    private Logger logger;

    public Log4jLogTool(Logger logger) {
        this.logger = logger;
    }

    public boolean isTraceEnable() {
        return logger.isTraceEnabled();
    }

    public boolean isDebugEnable() {
        return logger.isDebugEnabled();
    }

    public boolean isInfoEnable() {
        return logger.isInfoEnabled();
    }

    public boolean isWarnEnable() {
        return true;
    }

    public void trace(String message) {
        logger.trace(message);
    }

    public void trace(String message, Throwable t) {
        logger.trace(message, t);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void debug(String message, Throwable t) {
        logger.debug(message, t);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String message, Throwable t) {
        logger.info(message, t);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void warn(String message, Throwable t) {
        logger.warn(message, t);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable t) {
        logger.error(message, t);
    }
}
