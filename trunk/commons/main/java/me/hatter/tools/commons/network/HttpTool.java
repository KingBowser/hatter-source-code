package me.hatter.tools.commons.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.StringUtil;

public class HttpTool {

    private static final LogTool log = LogTools.getLogTool(HttpTool.class);

    public static class KeyValue {

        private String key;
        private String value;

        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    private int            connTimeoutInMillis = -1;
    private int            readTimeoutInMillis = -1;
    private String         charset             = "UTF-8";
    private String         url;
    private List<KeyValue> params              = new ArrayList<KeyValue>();

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

    public HttpTool url(String url) {
        this.url = url;
        return this;
    }

    public HttpTool addParam(KeyValue kv) {
        this.params.add(kv);
        return this;
    }

    public HttpTool addParams(List<KeyValue> params) {
        this.params.addAll(params);
        return this;
    }

    public String readFromURL() {
        return IOUtil.readToStringAndClose(readInputStreamFromURL(generateFullUrl(url, charset, params)), charset);
    }

    public InputStream readInputStreamFromURL(String url) {
        return readInputStreamFromURL(url, null);
    }

    public InputStream readInputStreamFromURL(String url, LinkedHashMap<String, List<String>> responseHeaderMap) {
        try {
            URLConnection conn = createURLConnection(new URL(url));
            if (responseHeaderMap != null) {
                responseHeaderMap.putAll(conn.getHeaderFields());
            }
            return conn.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String postAndReadFromURL(List<KeyValue> postParams) {
        return IOUtil.readToStringAndClose(postAndReadInputStreamFromURL(generateParams(charset, postParams)), charset);
    }

    public InputStream postAndReadInputStreamFromURL(String post) {
        return postAndReadInputStreamFromURL(generateFullUrl(url, charset, params), post, null);
    }

    public InputStream postAndReadInputStreamFromURL(String url, String post,
                                                     LinkedHashMap<String, List<String>> responseHeaderMap) {
        try {
            URLConnection conn = createURLConnection(new URL(url));
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream postOutputStream = conn.getOutputStream();
            try {
                postOutputStream.write(post.getBytes());
                postOutputStream.flush();
                if (responseHeaderMap != null) {
                    responseHeaderMap.putAll(conn.getHeaderFields());
                }
                return conn.getInputStream();
            } finally {
                postOutputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    URLConnection createURLConnection(URL u) throws IOException {
        URLConnection conn = u.openConnection();
        if (connTimeoutInMillis > 0) {
            conn.setConnectTimeout(connTimeoutInMillis);
        }
        if (readTimeoutInMillis > 0) {
            conn.setReadTimeout(readTimeoutInMillis);
        }
        return conn;
    }

    static String generateFullUrl(String url, String charset, List<KeyValue> params) {
        if ((url == null) || url.trim().isEmpty()) {
            throw new IllegalStateException("URL is null or empty.");
        }
        String param = generateParams(charset, params);
        url = url.trim();
        if (param.isEmpty()) {
            return url;
        }
        if (url.contains("?")) {
            if (url.endsWith("?")) {
                return url + param;
            } else {
                return url + "&" + param;
            }
        } else {
            if (url.endsWith("&")) {
                return url + param;
            } else {
                return url + "?" + param;
            }
        }
    }

    @SuppressWarnings("deprecation")
    static String generateParams(String charset, List<KeyValue> params) {
        List<String> ps = new ArrayList<String>(params.size());
        if (!params.isEmpty()) {
            for (KeyValue kv : params) {
                try {
                    ps.add(URLEncoder.encode(kv.getKey(), charset) + "=" + URLEncoder.encode(kv.getValue(), charset));
                } catch (UnsupportedEncodingException e) {
                    log.warn("Unsupported encoding exception:" + charset);
                    ps.add(URLEncoder.encode(kv.getKey()) + "=" + URLEncoder.encode(kv.getValue()));
                }
            }
        }
        return StringUtil.join(ps, "&");
    }
}
