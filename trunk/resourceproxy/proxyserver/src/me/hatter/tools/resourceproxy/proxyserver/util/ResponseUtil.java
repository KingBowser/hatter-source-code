package me.hatter.tools.resourceproxy.proxyserver.util;

import java.io.OutputStream;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class ResponseUtil {

    public static int STATUS_SUCCESS = 500;
    public static int STATUS_ERROR   = 500;

    @SuppressWarnings("restriction")
    public static void writeThrowableAndClose(HttpExchange exchange, Throwable t) {
        writeErrorAndClose(exchange, "Unknow error.\r\n\r\nExcepton: " + StringUtil.printStackTrace(t));
    }

    @SuppressWarnings("restriction")
    public static void writeErrorAndClose(HttpExchange exchange, String message) {
        try {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set(ContentTypes.CONTENT_TYPE, ContentTypes.PLAIN_AND_UTF8);
            exchange.sendResponseHeaders(HttpConstants.STATUS_SERVICE_ERROR, 0);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write((message).getBytes(ContentTypes.UTF8_CHARSET));
            responseBody.close();
        } catch (Exception e) {
            System.out.println("[ERROR] Write error to response failed. " + StringUtil.printStackTrace(e));
        }
    }
}
