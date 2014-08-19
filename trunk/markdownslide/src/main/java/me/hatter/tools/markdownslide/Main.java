package me.hatter.tools.markdownslide;

import java.util.Arrays;

import me.hatter.tools.commons.args.UnixArgsUtil;
import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspserver.handler.HttpServerHandler;
import me.hatter.tools.resourceproxy.jsspserver.main.MainHttpServer;

public class Main {

    private static final LogTool log = LogTools.getLogTool(Main.class);

    public static void main(String[] args) {
        ClassLoaderUtil.initLibResources();
        UnixArgsUtil.ARGS.addFSet("h", "help");
        UnixArgsUtil.ARGS.addFSet("s", "server");
        UnixArgsUtil.ARGS.addFSet("init-assets");
        UnixArgsUtil.parseGlobalArgs(args);

        if (UnixArgsUtil.ARGS.flags().containsAny("h", "help")) {
            System.out.println("");
            System.out.println("java -jar onehtmlall.jar [options]");
            System.out.println("     --h[elp]");
            System.exit(0);
        }

        JsspExecutor.initJsspWork();

        if (UnixArgsUtil.ARGS.flags().containsAny("s", "server")) {
            log.info("Start server mode...");
            String port = StringUtil.defaultValue(UnixArgsUtil.ARGS.kvalueAny("port"), "8000");
            MainHttpServer httpServer = new MainHttpServer(new HttpServerHandler(),
                                                           Arrays.asList(Integer.valueOf(port)));
            httpServer.run();
        } else {
            // TODO
        }
    }
}
