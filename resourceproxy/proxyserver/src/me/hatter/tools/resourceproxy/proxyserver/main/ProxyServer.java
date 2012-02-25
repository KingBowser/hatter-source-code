package me.hatter.tools.resourceproxy.proxyserver.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpObjectUtil;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpRequestUtil;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpResponseUtil;
import me.hatter.tools.resourceproxy.proxyserver.util.ResponseUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ProxyServer {

    private static Properties HOST_PROPERTIES = new Properties();
    static {
        if (System.getProperties().containsKey("debug")) {
            try {
                HOST_PROPERTIES.load(new FileInputStream("hosts.properties"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("restriction")
    public static void main(String[] args) throws Exception {
        InetSocketAddress addr = new InetSocketAddress(80);
        HttpServer httpServer = HttpServer.create(addr, 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(20));
        httpServer.createContext("/", new MyHandler());
        httpServer.start();
        System.out.println("Start server on: " + addr.getPort());
    }

    @SuppressWarnings("restriction")
    static class MyHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {
            try {
                HttpRequest request = HttpRequestUtil.build(exchange);
                if (request.getMethod().equals("GET")) {

                    String host = request.getHost();
                    if (host == null) {
                        System.out.println("No host assigned: " + request.getUri().toString());
                        ResponseUtil.writeErrorAndClose(exchange, "No host assigned: " + request.getUri().toString());
                        return; // ERROR and RETURN
                    }

                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    long startMills = System.currentTimeMillis();
                    String u = "http://" + host + request.getUri().toString();
                    System.out.println("Request: " + request.getMethod() + " " + u + " #" + request.getRemoteAddress());
                    if ("localhost".equals(host) || host.matches("\\d+(\\.\\d+){3}(:\\d+)?")) {
                        System.out.println("Ignore IP request.");
                        ResponseUtil.writeErrorAndClose(exchange, "Ignore IP request: " + request.getMethod() + " " + u);
                        return; // ERROR and RETURN
                    }
                    String realHost = null;
                    if (HOST_PROPERTIES.containsKey(host)) {
                        realHost = host;
                        u = "http://" + HOST_PROPERTIES.getProperty(host) + request.getUri().toString();
                    }
                    URL url = new URL(u);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod(request.getMethod());
                    System.out.println("Request headers: ");
                    for (String key : request.getHeaderMap().keySet()) {
                        for (String value : request.get(key)) {
                            System.out.println("    " + key + ": " + value);
                            httpURLConnection.addRequestProperty(key, value);
                        }
                    }
                    if (realHost != null) {
                        httpURLConnection.addRequestProperty("Host", realHost);
                    }
                    httpURLConnection.setUseCaches(false);

                    HttpResponse response = HttpResponseUtil.build(httpURLConnection);

                    HttpObject httpObject = HttpObjectUtil.frHttpRequest(request, response);
                    HttpObject httpObjectFromDB = DataAccessObject.selectObject(httpObject);
                    if (httpObjectFromDB == null) {
                        try {
                            DataAccessObject.insertObject(httpObject);
                        } catch (Exception e) {
                            System.out.println("[ERROR] insert data error " + httpObject.getUrl() + " @"
                                               + httpObject.getAccessAddress() + " /" + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        DataAccessObject.updateObject(httpObject);
                    }

                    System.out.println("Status: " + response.getStatus());

                    Headers responseHeaders = exchange.getResponseHeaders();
                    System.out.println("Headers: ");

                    for (String key : response.getHeaderMap().keySet()) {
                        if (key != null) {
                            responseHeaders.put(key, new ArrayList<String>(response.get(key)));
                        }
                    }
                    responseHeaders.put("Content-Length",
                                        new ArrayList<String>(Arrays.asList(String.valueOf(response.getBytes()))));
                    exchange.sendResponseHeaders(response.getStatus(), 0);

                    System.out.println("Body-Commit-Length: " + response.getBytes().length);
                    System.out.println("Cost-Time: " + (System.currentTimeMillis() - startMills) + "ms");
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(response.getBytes());

                    responseBody.close();
                } else {
                    System.out.println("Not supported method: " + request.getMethod());
                    ResponseUtil.writeErrorAndClose(exchange, "Not supported method: " + request.getMethod());
                }
            } catch (Throwable t) {
                t.printStackTrace();
                ResponseUtil.writeThrowableAndClose(exchange, t);
            }
        }
    }
}
