package me.hatter.tools.markdowndocs;

import java.io.File;
import java.io.IOException;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdowndocs.config.GlobalInit;
import me.hatter.tools.markdowndocs.config.GlobalVars;

public class Main {

    private static final LogTool log = LogTools.getLogTool(Main.class);

    // -d dir
    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);
        if (StringUtil.isNotEmpty(UnixArgsutil.ARGS.kvalue("d"))) {
            GlobalVars.setBasePath(new File(UnixArgsutil.ARGS.kvalue("d")));
        }

        try {
            GlobalInit.initAssets();
        } catch (IOException e) {
            log.error("Error!!", e);
        }
    }
}
