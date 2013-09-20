package me.hatter.tools.commons.log.spi.impl;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.MDCTool;
import me.hatter.tools.commons.log.annotation.AutoInit;
import me.hatter.tools.commons.log.impl.Log4jLogTool;
import me.hatter.tools.commons.log.impl.Log4jMDCTool;
import me.hatter.tools.commons.log.spi.LogToolProvider;

import org.apache.log4j.Logger;

@AutoInit
public class Log4jLogToolProvider implements LogToolProvider {

    private static final MDCTool MDC_TOOL = new Log4jMDCTool();

    public LogTool provide(String name) {
        return new Log4jLogTool(Logger.getLogger(name));
    }

    public MDCTool provideMDC() {
        return MDC_TOOL;
    }
}
