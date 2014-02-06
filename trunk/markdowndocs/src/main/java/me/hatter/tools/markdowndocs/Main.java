package me.hatter.tools.markdowndocs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hatter.tools.commons.args.UnixArgsUtil;
import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.resource.impl.URLResource;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdowndocs.config.Config;
import me.hatter.tools.markdowndocs.config.GlobalInit;
import me.hatter.tools.markdowndocs.config.GlobalVars;
import me.hatter.tools.markdowndocs.model.MenuItem;
import me.hatter.tools.markdowndocs.model.Page;
import me.hatter.tools.markdowndocs.template.ConfigParser;
import me.hatter.tools.markdowndocs.template.MenuParser;
import me.hatter.tools.markdowndocs.template.PageParser;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;
import me.hatter.tools.resourceproxy.jsspserver.handler.HttpServerHandler;
import me.hatter.tools.resourceproxy.jsspserver.main.MainHttpServer;

public class Main {

    private static final LogTool log = LogTools.getLogTool(Main.class);

    // -d dir
    public static void main(String[] args) {
        ClassLoaderUtil.initLibResources();
        UnixArgsUtil.ARGS.addFSet("h", "help");
        UnixArgsUtil.ARGS.addFSet("s", "server");
        UnixArgsUtil.parseGlobalArgs(args);

        if (UnixArgsUtil.ARGS.flags().containsAny("h", "help")) {
            System.out.println("");
            System.out.println("java -jar markdowndocsall.jar [options]");
            System.out.println("     --h[elp]");
            System.out.println("     -d dir");
            System.exit(0);
        }

        if (StringUtil.isNotEmpty(UnixArgsUtil.ARGS.kvalueAny("d", "dir"))) {
            GlobalVars.setBasePath(new File(UnixArgsUtil.ARGS.kvalue("d", "dir")));
        }

        File markdowndocs = new File(GlobalVars.getBasePath(), ".markdowndocs");
        if (!markdowndocs.exists()) {
            log.error("Markdowndocs root mark file not found! `touch .markdowndocs`");
            System.exit(-1);
        }

        JsspExecutor.initJsspWork();

        if (UnixArgsUtil.ARGS.flags().containsAny("s", "server")) {
            log.info("Start server mode...");
            String port = StringUtil.defaultValue(UnixArgsUtil.ARGS.kvalueAny("p", "port"), "8000");
            MainHttpServer httpServer = new MainHttpServer(new HttpServerHandler(),
                                                           Arrays.asList(Integer.valueOf(port)));
            httpServer.run();
        } else {
            staticInitPages();
        }
    }

    private static void staticInitPages() {
        try {
            GlobalInit.initAssets();
        } catch (IOException e) {
            log.error("Error!!", e);
        }

        List<MenuItem> refLefts = new ArrayList<MenuItem>();
        List<MenuItem> refRights = new ArrayList<MenuItem>();
        MenuParser.parseMenuItems(refLefts, refRights);

        List<MenuItem> allItems = new ArrayList<MenuItem>();
        allItems.addAll(refLefts);
        allItems.addAll(refRights);
        for (MenuItem item : allItems) {
            File dir = (StringUtil.isEmpty(item.getDirName())) ? GlobalVars.getBasePath() : new File(
                                                                                                     GlobalVars.getBasePath(),
                                                                                                     item.getDirName());
            Config config = ConfigParser.readConfig(item.getDirName());
            Page page = PageParser.parsePage(item.getDirName());
            page.setLefts(refLefts);
            page.setRights(refRights);

            Map<String, Object> addContext = new HashMap<String, Object>();
            addContext.put("config", config);
            addContext.put("page", page);

            BufferWriter bw = new BufferWriter();
            URLResource resource = new URLResource(TestMain.class.getResource("/templates/main.template.jssp"),
                                                   "main.template.jssp");
            JsspExecutor.executeJssp(resource, new HashMap<String, Object>(), addContext, null, bw);

            log.info("Write html file: " + new File(dir, "index.htm"));
            FileUtil.writeStringToFile(new File(dir, "index.htm"), bw.getBufferedString());
        }
    }
}
