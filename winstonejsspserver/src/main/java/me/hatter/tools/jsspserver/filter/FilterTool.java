package me.hatter.tools.jsspserver.filter;

import java.io.File;

import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.commons.resource.Resources;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;

public class FilterTool {

    public static final String  DEFAULT_CHARACTER_ENCODING = "UTF-8";

    public static final boolean JSSP_DEBUG                 = Boolean.valueOf(System.getProperty("jsspdebug"));
    public static final String  JSSP_PATH                  = System.getProperty("jssp.path");

    public File                 jsspPath;
    public Resources            resources;

    private static FilterTool   defaultInstance;

    synchronized public static void initDefaultInstance(File jsspPath) {
        if ((JSSP_PATH == null) && (defaultInstance == null)) {
            defaultInstance = new FilterTool(jsspPath);
        }
    }

    public static void initDefaultInstance(String jsspPath) {
        initDefaultInstance(new File(jsspPath));
    }

    synchronized public static FilterTool defaultInstance() {
        if (defaultInstance != null) {
            return defaultInstance;
        }
        defaultInstance = new FilterTool();
        return defaultInstance;
    }

    public FilterTool() {
        if (JSSP_PATH == null) {
            this.jsspPath = new File(System.getProperty("user.dir"), "_jssp");
        } else {
            this.jsspPath = new File(JSSP_PATH);
        }
        this.resources = new Resources(this.jsspPath);
    }

    public FilterTool(File jsspPath) {
        this.jsspPath = jsspPath;
        this.resources = new Resources(this.jsspPath);
    }

    static {
        JsspExecutor.initJsspWork();
    }

    public Resource getResource(String fpath) {
        return resources.findResource(Resources.RESOURCE + dealFPath(fpath));
    }

    public static String dealFPath(String fpath) {
        if (fpath == null) {
            return null;
        }
        if (!Resources.isDefClasspath()) {
            return fpath;
        }
        fpath = fpath.replace('\\', '/').trim();
        while (fpath.startsWith("/")) {
            fpath = fpath.substring(1);
        }
        return fpath;
    }
}
