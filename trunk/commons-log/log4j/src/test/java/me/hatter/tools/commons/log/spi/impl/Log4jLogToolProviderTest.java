package me.hatter.tools.commons.log.spi.impl;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;

public class Log4jLogToolProviderTest {

    private static final LogTool logTool = LogTools.getLogTool(Log4jLogToolProviderTest.class);

    public static void main(String[] args) {
        logTool.trace("Trace test!");
        logTool.info("Info test!");
        logTool.debug("Debug test!");
        logTool.warn("Warn test!");
        logTool.error("Error test!");
    }
}
