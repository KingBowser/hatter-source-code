package me.hatter.tools.resourceproxy.httpobjects.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.commons.util.IOUtil;
import me.hatter.tools.resourceproxy.commons.util.KeyValueListMap;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class HttpRequestUtil {

    private static Set<String> IGNORE_HEADER_SET = new HashSet<String>(
                                                                       CollUtil.toUpperCase(Arrays.asList("If-modified-since", // ,
                                                                                                          "If-none-match")));

    @SuppressWarnings("restriction")
    public static HttpRequest build(HttpExchange exchange) {
        int uploadCount = 0;
        HttpRequest request = new HttpRequest();
        request.setMethod(exchange.getRequestMethod().toUpperCase());
        request.setUri(exchange.getRequestURI());
        request.setRemoteAddress(exchange.getRemoteAddress());
        Headers requestHeaders = exchange.getRequestHeaders();
        uploadCount += (3 + 7 + 2 + exchange.getRequestURI().toString().length());
        boolean isMultipartFormData = false;
        for (String key : requestHeaders.keySet()) {
            if (!IGNORE_HEADER_SET.contains(StringUtil.toUpperCase(key))) {
                List<String> values = requestHeaders.get(key);
                request.set(key, values);
                uploadCount += key.length();
                for (String v : values) {
                    uploadCount += v.length();
                }
            }
            if ("Content-Type".equalsIgnoreCase(key)) {
                if (requestHeaders.getFirst(key).toLowerCase().startsWith("multipart/form-data")) {
                    isMultipartFormData = true;
                }
            }
        }
        request.setHost(request.getHost());
        request.setFullUrl("http://" + request.getHost()
                           + ((request.getPort() == null) ? "" : (":" + request.getPort()))
                           + request.getUri().toString());
        request.setUploadCount(uploadCount);

        String query = exchange.getRequestURI().getRawQuery();
        try {
            parseKVListMap(request.getQueryMap(), query);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtil.copy(exchange.getRequestBody(), baos);
                request.setPostBytes(baos.toByteArray());
                if (isMultipartFormData) {
                    System.out.println("[WARN] The request is multiplepart/form-data: " + request.getFullUrl());
                } else {
                    String post = new String(baos.toByteArray(), "UTF-8");
                    parseKVListMap(request.getPostMap(), post);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        KeyValueListMap queryValueMap = new KeyValueListMap(request.getQueryMap());
        queryValueMap.addMap(request.getPostMap());
        request.setQueryValueMap(queryValueMap);

        return request;
    }

    private static void parseKVListMap(KeyValueListMap kvlMap, String query) throws IOException {
        if (query == null) {
            return;
        }
        String[] ql = query.split("&");
        for (String kev : ql) {
            if (kev.length() > 0) {
                int indexOfE = kev.indexOf('=');
                if (indexOfE < 0) {
                    kvlMap.add(kev, "");
                } else {
                    String k = kev.substring(0, indexOfE);
                    String v = kev.substring(indexOfE + 1);
                    kvlMap.add(k, URLDecoder.decode(v, "UTF-8"));
                }
            }
        }
    }
}
