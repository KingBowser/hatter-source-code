package me.hatter.tools.jsspserver.filter;

import java.io.File;

import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.commons.resource.Resources;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;

public class FilterUtil {

    public static final String  DEFAULT_CHARACTER_ENCODING = "UTF-8";

    public static final boolean JSSP_DEBUG                 = Boolean.valueOf(System.getProperty("jsspdebug"));

    public static File          JSSP_PATH;
    static {
        String jsspPath = System.getProperty("jssp.path");
        if (jsspPath == null) {
            JSSP_PATH = new File(System.getProperty("user.dir"), "_jssp");
        } else {
            JSSP_PATH = new File(jsspPath);
        }
    }
    public static Resources     RESOURCES                  = new Resources(JSSP_PATH);

    static {
        JsspExecutor.initJsspWork();
    }

    public static Resource getResource(String fpath) {
        return RESOURCES.findResource(Resources.RESOURCE + dealFPath(fpath));
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
