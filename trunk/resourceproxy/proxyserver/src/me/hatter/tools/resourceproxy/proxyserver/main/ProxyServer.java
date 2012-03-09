package me.hatter.tools.resourceproxy.proxyserver.main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import me.hatter.tools.resourceproxy.commons.util.CollUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpRequestUtil;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.proxyserver.util.ResponseUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ProxyServer {

    private static AtomicLong TOTAL_UPLOAD_COUNT = new AtomicLong(0);

    @SuppressWarnings("restriction")
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        InetSocketAddress addr = new InetSocketAddress(80);
        HttpServer httpServer = HttpServer.create(addr, 0);
        if (System.getProperties().containsKey("debug")) {
            httpServer.setExecutor(Executors.newFixedThreadPool(1));
        } else {
            httpServer.setExecutor(Executors.newFixedThreadPool(12));
        }
        httpServer.createContext("/", new MyHandler());
        httpServer.start();
        System.out.println("Start server on: " + addr.getPort() + " cost: " + (System.currentTimeMillis() - start)
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
                HttpResponse response = ResourceFilterChain.filterChain(request);
                writeResponse(exchange, startMills, response);
                System.out.println("[INFO] >>>> Total upload bytes: " + TOTAL_UPLOAD_COUNT.get());
            } catch (Throwable t) {
                System.out.println("[ERROR] Exception occured: ");
                t.printStackTrace();
                ResponseUtil.writeThrowableAndClose(exchange, t);
            }
        }

        private void writeResponse(HttpExchange exchange, long startMills, HttpResponse response)
                                                                                                 throws UnsupportedEncodingException,
                                                                                                 IOException {
            byte[] theBytes = response.getBytes();
            if (response.getString() != null) {
                String charset = (response.getCharset() == null) ? "UTF-8" : response.getCharset();
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

            responseHeaders.put("X-Powered-Server", CollUtil.objectToList("ResourceProxy_By_Hatter/0.1"));
            if (!response.isFromNetwork()) {
                responseHeaders.put("X-Powered-Cache", CollUtil.objectToList("DB"));
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
}
