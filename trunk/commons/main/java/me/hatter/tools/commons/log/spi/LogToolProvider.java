package me.hatter.tools.commons.log.spi;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.MDCTool;

public interface LogToolProvider {

    LogTool provide(String name);

    MDCTool provideMDC();
}
