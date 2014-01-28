package me.hatter.tools.markdowndocs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.commons.resource.impl.URLResource;
import me.hatter.tools.markdowndocs.config.Config;
import me.hatter.tools.markdowndocs.config.GlobalVars;
import me.hatter.tools.markdowndocs.model.Page;
import me.hatter.tools.markdowndocs.template.ConfigParser;
import me.hatter.tools.markdowndocs.template.PageParser;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.utl.BufferWriter;

public class TestMain {

    public static void main(String[] args) {
        GlobalVars.setBasePath(new File("/Users/hatterjiang/Code/hatter-source-code/markdowndocs/samplex"));
        Config config = ConfigParser.readConfig("01-Top10");
        Page page = PageParser.parsePage("01-Top10");

        JsspExecutor.initJsspWork();

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("config", config);
        context.put("page", page);

        BufferWriter bw = new BufferWriter();
        URLResource resource = new URLResource(TestMain.class.getResource("/templates/main.template.jssp"),
                                               "main.template.jssp");
        JsspExecutor.executeJssp(resource, new HashMap<String, Object>(), context, null, bw);

        System.out.println(bw.getBufferedString());
    }
}
