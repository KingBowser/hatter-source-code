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
        return defaultInstance().charset(charset);
    }

    public static HttpTool defaultInstance() {
        return new HttpTool().connTimeout(10000).readTimeout(100000);
    }

    public HttpTool connTimeout(int connTimeoutInMillis) {
        this.connTimeoutInMillis = connTimeoutInMillis;
        return this;
    }

    public HttpTool readTimeout(int readTimeoutInMillis) {
        this.readTimeoutInMillis = readTimeoutInMillis;
        return this;
    }

    public HttpTool charset(String charset) {
        this.charset = charset;
        return this;
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
