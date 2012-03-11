package me.hatter.tools.resourceproxy.jsspserver.util;

import java.io.IOException;
import java.util.Properties;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.jsspserver.filter.impl.DefaultFileFilter;

public class ContentTypes {

    public static final String      DEFAULT_CONTENT_TYPE    = "application/octet-stream";
    public static final String      HTML_CONTENT_TYPE       = "text/html";
    public static final String      PLAIN_CONTENT_TYPE      = "text/plain";
    public static final String      JAVASCRIPT_CONTENT_TYPE = "application/javascript";

    public static final String      UTF8_CHARSET            = "UTF-8";
    public static final String      HTML_AND_UTF8           = HTML_CONTENT_TYPE + ";charset=" + UTF8_CHARSET;
    public static final String      PLAIN_AND_UTF8          = PLAIN_CONTENT_TYPE + ";charset=" + UTF8_CHARSET;
    public static final String      JAVASCRIPT_AND_UTF8     = JAVASCRIPT_CONTENT_TYPE + ";charset=" + UTF8_CHARSET;

    public static final String      CONTENT_TYPE            = "Content-Type";

    private static final Properties CONTENT_TYPE_PROPERTIES = new Properties();
    static {
        try {
            CONTENT_TYPE_PROPERTIES.load(DefaultFileFilter.class.getResourceAsStream("/content-type.properties"));
        } catch (IOException e) {
            System.err.println("[ERROR] Error in load content-type.properties: " + StringUtil.printStackTrace(e));
        }
    }

    public static String getContentTypeByExt(String ext) {
        if (StringUtil.isEmpty(ext)) {
            return null;
        }
        String contentType = CONTENT_TYPE_PROPERTIES.getProperty(ext.trim().toLowerCase());
        return (contentType == null) ? DEFAULT_CONTENT_TYPE : contentType;
    }
}
