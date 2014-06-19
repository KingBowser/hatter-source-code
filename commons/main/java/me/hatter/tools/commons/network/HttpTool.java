package me.hatter.tools.commons.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import me.hatter.tools.commons.io.IOUtil;

public class HttpTool {

    private int    connTimeoutInMillis = -1;
    private int    readTimeoutInMillis = -1;
    private String charset             = "UTF-8";

    public static HttpTool defaultInstance(String charset) {
        HttpTool tool = defaultInstance();
        tool.charset = charset;
        return tool;
    }

    public static HttpTool defaultInstance() {
        HttpTool tool = new HttpTool();
        tool.connTimeoutInMillis = 10000;
        tool.readTimeoutInMillis = 100000;
        return tool;
    }

    public String readFromURL(String url) {
        return IOUtil.readToStringAndClose(readInputStreamFromURL(url), charset);
    }

    public InputStream readInputStreamFromURL(String url) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            if (connTimeoutInMillis > 0) {
                conn.setConnectTimeout(connTimeoutInMillis);
            }
            if (readTimeoutInMillis > 0) {
                conn.setReadTimeout(readTimeoutInMillis);
            }
            return conn.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
