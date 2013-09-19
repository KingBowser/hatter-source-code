package me.hatter.tools.commons.log.impl;

import me.hatter.tools.commons.log.LogTools;

public class LogUtilLogToolTest {

    public static void main(String[] args) {
        LogTools.getLogTool(LogUtilLogToolTest.class).info("test info");
    }
}
