package me.hatter.tools.resourceproxy.proxyserver.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class ResponseUtil {

    public static int STATUS_SUCCESS = 500;
    public static int STATUS_ERROR   = 500;

    @SuppressWarnings("restriction")
    public static void writeThrowableAndClose(HttpExchange exchange, Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        writeErrorAndClose(exchange, "Unknow error.\r\n\r\nExcepton: " + sw.toString());
    }

    @SuppressWarnings("restriction")
    public static void writeErrorAndClose(HttpExchange exchange, String message) {
        try {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain;charset=UTF-8");
            exchange.sendResponseHeaders(500, 0);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write((message).getBytes("UTF-8"));
            responseBody.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
