package me.hatter.tools.markdownslide;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.commons.args.UnixArgsUtil;
import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.resource.impl.URLResource;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdownslide.config.Configs;
import me.hatter.tools.markdownslide.filter.MarkdownSlideFilter;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.util.BufferWriter;
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
            System.out.println("     -h[elp]      help docs");
            System.out.println("     -s[erver]    run as server mode");
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
            try {
                File slidesMd = new File(Environment.USER_DIR, "slides.md");
                if (!slidesMd.exists()) {
                    throw new RuntimeException("File `slides.md` not found!");
                }

                Map<String, Object> addContext = new HashMap<String, Object>();
                addContext.put("config", Configs.getConfig());
                addContext.put("inline_css",
                               IOUtil.readToStringAndClose(new FileInputStream(MarkdownSlideFilter.initInlineCss())));
                addContext.put("slides_md", IOUtil.readToStringAndClose(new FileInputStream(slidesMd)));

                BufferWriter bw = new BufferWriter();
                URLResource resource = new URLResource(
                                                       MarkdownSlideFilter.class.getResource("/"
                                                                                             + Configs.getConfig().getTemplate()
                                                                                             + "/templates/template.jssp"),
                                                       "template.jssp");
                JsspExecutor.executeJssp(resource, new HashMap<String, Object>(), addContext, null, bw);
                FileUtil.writeStringToFile(new File(Environment.USER_DIR, "index.htm"), bw.getBufferedString());
            } catch (Exception e) {
                log.error("Generate slide index.htm faild!", e);
            }
        }
    }
}
