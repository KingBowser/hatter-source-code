package me.hatter.tools.commons.log.impl;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogUtil;

@SuppressWarnings("deprecation")
public class LogUtilLogTool implements LogTool {

    @Override
    public boolean isTraceEnable() {
        return LogUtil.isTraceEnable();
    }

    @Override
    public boolean isDebugEnable() {
        return LogUtil.isDebugEnable();
    }

    @Override
    public boolean isInfoEnable() {
        return LogUtil.isInfoEnable();
    }

    @Override
    public boolean isWarnEnable() {
        return LogUtil.isWarnEnable();
    }

    @Override
    public void trace(String message) {
        LogUtil.trace(message);
    }

    @Override
    public void trace(String message, Throwable t) {
        LogUtil.trace(message, t);
    }

    @Override
    public void debug(String message) {
        LogUtil.debug(message);
    }

    @Override
    public void debug(String message, Throwable t) {
        LogUtil.debug(message, t);
    }

    @Override
    public void info(String message) {
        LogUtil.info(message);
    }

    @Override
    public void info(String message, Throwable t) {
        LogUtil.info(message, t);
    }

    @Override
    public void warn(String message) {
        LogUtil.warn(message);
    }

    @Override
    public void warn(String message, Throwable t) {
        LogUtil.warn(message, t);
    }

    @Override
    public void error(String message) {
        LogUtil.error(message);
    }

    @Override
    public void error(String message, Throwable t) {
        LogUtil.error(message, t);
    }
}
