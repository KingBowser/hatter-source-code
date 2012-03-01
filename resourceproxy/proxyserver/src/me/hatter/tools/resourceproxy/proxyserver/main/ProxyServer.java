package me.hatter.tools.resourceproxy.proxyserver.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
        if (System.getProperties().containsKey("debug")) {
            httpServer.setExecutor(Executors.newFixedThreadPool(1));
        } else {
            httpServer.setExecutor(Executors.newFixedThreadPool(12));
        }
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
                    String u = request.getFullUrl();
                    System.out.println("Request: " + request.getMethod() + " " + u + " #" + request.getRemoteAddress());
                    if ("localhost".equals(host) || host.matches("\\d+(\\.\\d+){3}(:\\d+)?")) {
                        System.out.println("Ignore IP request.");
                        ResponseUtil.writeErrorAndClose(exchange, "Ignore IP request: " + request.getMethod() + " " + u);
                        return; // ERROR and RETURN
                    }
                    HttpObject queryHttpObject = new HttpObject();
                    queryHttpObject.setUrl(request.getFullUrl());
                    queryHttpObject.setAccessAddress(request.getIp());
                    HttpObject httpObjectFromDBFirst = DataAccessObject.selectObject(queryHttpObject);
                    if (httpObjectFromDBFirst == null) {
                        System.out.println("[INFO] Http Object from db first is null.");
                    }

                    boolean isFromNetWork;
                    HttpResponse response = null;
                    if (httpObjectFromDBFirst != null) {
                        System.out.println("[INFO] Http Object deserilize from db.");
                        isFromNetWork = false;
                        response = HttpObjectUtil.toHttpRequest(request, httpObjectFromDBFirst);
                    } else {
                        isFromNetWork = true;
                        response = getHttpResponseFromNetwork(request, host, u);
                    }

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

                    responseHeaders.put("X-Powered-Server",
                                        new ArrayList<String>(Arrays.asList("ResourceProxy_By_Hatter/0.1")));
                    if (!isFromNetWork) {
                        responseHeaders.put("X-Powered-Cache", new ArrayList<String>(Arrays.asList("DB")));
                    }
                    exchange.sendResponseHeaders(response.getStatus(), 0);

                    System.out.println("[INFO] Response Body-Commit-Length: " + theBytes.length);
                    System.out.println("[INFO] Response Cost-Time: " + (System.currentTimeMillis() - startMills) + "ms");
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(theBytes);

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

        private HttpResponse getHttpResponseFromNetwork(HttpRequest request, String host, String u)
                                                                                                   throws MalformedURLException,
                                                                                                   IOException,
                                                                                                   ProtocolException {
            HttpResponse response;
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

            response = HttpResponseUtil.build(httpURLConnection);

            HttpObject httpObject = HttpObjectUtil.frHttpRequest(request, response);
            HttpObject httpObjectFromDB = DataAccessObject.selectObject(httpObject);
            if (httpObjectFromDB == null) {
                System.out.println("[INFO] Http Object from db is null.");
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
            return response;
        }
    }
}
