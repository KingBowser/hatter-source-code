package me.hatter.tools.resourceproxy.proxyserver.main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpRequestUtil;
import me.hatter.tools.resourceproxy.jsspserver.filter.DefaultResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.proxyserver.util.ResponseUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ProxyServer {

    public static final String        HEADER_X_REQUEST_BY     = "X-Request-By";
    public static final String        HEADER_X_POWERED_SERVER = "X-Powered-Server";
    public static final String        HEADER_X_POWERED_CACHE  = "X-Powered-Cache";
    public static final String        PROXY_SERVER_VERSION    = "ResourceProxy/0.2";
    public static final List<Integer> DEFAULT_PORTS           = Arrays.asList(80, 8080, 2080, 3080);
    public static final List<Integer> PORTS                   = getIntPropertyList("ports", DEFAULT_PORTS);
    private static final AtomicLong   TOTAL_UPLOAD_COUNT      = new AtomicLong(0);

    @SuppressWarnings("restriction")
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        for (int port : PORTS) {
            try {
                InetSocketAddress addr = new InetSocketAddress(port);
                HttpServer httpServer = HttpServer.create(addr, 0);
                if (System.getProperties().containsKey("debug")) {
                    httpServer.setExecutor(Executors.newFixedThreadPool(1));
                } else {
                    httpServer.setExecutor(Executors.newFixedThreadPool(12));
                }
                httpServer.createContext("/", new MyHandler());
                httpServer.start();
            } catch (BindException be) {
                System.out.println("[ERROR] Bind port failed: " + port);
            }
        }
        System.out.println("[INFO] Start ProxyServer on: " + PORTS + " cost: " + (System.currentTimeMillis() - start)
                           + " ms");
    }

    @SuppressWarnings("restriction")
    static class MyHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {
            try {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                HttpRequest request = HttpRequestUtil.build(exchange);
                System.out.println("[INFO] Request: " + request.getMethod() + " " + request.getFullUrl() + " #"
                                   + request.getRemoteAddress());

                TOTAL_UPLOAD_COUNT.addAndGet((long) request.getUploadCount());
                long startMills = System.currentTimeMillis();
                HttpResponse response = DefaultResourceFilterChain.filterChain(request);
                writeResponse(exchange, startMills, response);
                System.out.println("[INFO] >>>> Total upload bytes: " + TOTAL_UPLOAD_COUNT.get());
            } catch (Throwable t) {
                System.out.println("[ERROR] Exception occured: " + StringUtil.printStackTrace(t));
                ResponseUtil.writeThrowableAndClose(exchange, t);
            }
        }

        private void writeResponse(HttpExchange exchange, long startMills, HttpResponse response)
                                                                                                 throws UnsupportedEncodingException,
                                                                                                 IOException {
            byte[] theBytes = response.getBytes();
            if (response.getString() != null) {
                String charset = (response.getCharset() == null) ? ContentTypes.UTF8_CHARSET : response.getCharset();
                theBytes = response.getString().getBytes(charset);
            }

            System.out.println("[INFO] Response status: " + response.getStatus());

            Headers responseHeaders = exchange.getResponseHeaders();
            System.out.println("[INFO] Response headers: ");

            System.out.println("[INFO] Header Map: " + response.getHeaderMap());
            for (String key : response.getHeaderMap().keySet()) {
                if (key != null) {
                    responseHeaders.put(key, new ArrayList<String>(response.get(key)));
                }
            }
            // we are using chunked, so content-length is useless
            // responseHeaders.put("Content-Length", new
            // ArrayList<String>(Arrays.asList(String.valueOf(theBytes.length))));

            responseHeaders.put(HEADER_X_POWERED_SERVER, CollUtil.objectToList(PROXY_SERVER_VERSION));
            if (!response.isFromNetwork()) {
                responseHeaders.put(HEADER_X_POWERED_CACHE, CollUtil.objectToList("DB"));
            }
            exchange.sendResponseHeaders(response.getStatus(), 0);

            System.out.println("[INFO] Response Body-Commit-Length: " + ((theBytes == null) ? 0 : theBytes.length));
            System.out.println("[INFO] Response Cost-Time: " + (System.currentTimeMillis() - startMills) + "ms");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

            OutputStream responseBody = exchange.getResponseBody();
            if (theBytes != null) {
                responseBody.write(theBytes);
            }

            responseBody.close();
        }
    }

    public static List<Integer> getIntPropertyList(String key, List<Integer> defaultValue) {
        return getIntPropertyList(key, ",", defaultValue);
    }

    public static List<Integer> getIntPropertyList(String key, String regexSeparater, List<Integer> defaultValue) {
        List<String> result = getInnerStrPropertyList(key, regexSeparater);
        List<Integer> intListResult = new ArrayList<Integer>();
        if (result != null) {
            for (String v : result) {
                try {
                    Integer i = Integer.valueOf(v);
                    intListResult.add(i);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot parse property key/value: " + key + " / " + v + " of: " + result);
                }
            }
        }
        intListResult = intListResult.isEmpty() ? defaultValue : intListResult;
        return intListResult;
    }

    private static List<String> getInnerStrPropertyList(String key, String regexSeparater) {
        String value = System.getProperty(key);
        if (value == null) {
            return null;
        }
        String[] vals = value.split(regexSeparater);
        List<String> result = new ArrayList<String>();
        for (String v : vals) {
            v = v.trim();
            if (!v.isEmpty()) {
                result.add(v);
            }
        }
        return result.isEmpty() ? null : result;
    }
}
