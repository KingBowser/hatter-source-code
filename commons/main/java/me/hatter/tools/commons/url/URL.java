package me.hatter.tools.commons.url;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.string.StringUtil;

public class URL {

    private String       encode;
    private String       schema;
    private String       host;
    private Integer      port;
    private List<String> paths;
    private List<Param>  params;

    public URL() {
    }

    public static URL ins() {
        return instance();
    }

    public static URL ins(String schema) {
        return instance(schema);
    }

    public static URL instance() {
        return new URL();
    }

    public static URL instance(String schema) {
        return new URL().schema(schema);
    }

    public static URL http() {
        return new URL().schema("http");
    }

    public static URL http(String host) {
        return new URL().schema("http").host(host);
    }

    public static URL https() {
        return new URL().schema("https");
    }

    public static URL https(String host) {
        return new URL().schema("https").host(host);
    }

    public URL encode(String encode) {
        this.encode = encode;
        return this;
    }

    public URL schema(String schema) {
        this.schema = schema;
        return this;
    }

    public URL host(String host) {
        this.host = host;
        return this;
    }

    public URL port(int port) {
        this.port = port;
        return this;
    }

    public URL path(String path) {
        if (paths == null) {
            paths = new ArrayList<String>();
        }
        if (path.contains("/")) {
            this.paths.addAll(Arrays.asList(path.split("\\/")));
        } else {
            this.paths.add(path);
        }
        return this;
    }

    public URL param(String key, String value) {
        return param(new Param(key, value));
    }

    public URL param(Param param) {
        if (params == null) {
            params = new ArrayList<Param>();
        }
        this.params.add(param);
        return this;
    }

    public java.net.URL toURL() {
        try {
            return new java.net.URL(generateStrURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateStrURL() {
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotEmpty(host)) {
            if (StringUtil.isNotEmpty(schema)) {
                sb.append(schema);
                if (!schema.endsWith("://")) {
                    sb.append("://");
                }
            } else {
                sb.append("//");
            }
            sb.append(host);
            if (port != null) {
                sb.append(":").append(port);
            }
        }
        sb.append("/");
        if ((paths != null) && (!paths.isEmpty())) {
            List<String> encPaths = CollectionUtil.transform(paths, new CollectionUtil.Transformer<String, String>() {

                @Override
                public String transform(String object) {
                    return URLUtil.encode(object, encode);
                }
            });
            sb.append(StringUtil.join(encPaths, "/"));
        }
        if ((params != null) && (!params.isEmpty())) {
            sb.append("?");
            for (Param p : params) {
                sb.append(URLUtil.encode(p.getKey(), encode)).append("=").append(URLUtil.encode(p.getValue(), encode));
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return generateStrURL();
    }
}
