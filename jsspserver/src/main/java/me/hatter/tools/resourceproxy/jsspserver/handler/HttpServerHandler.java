package me.hatter.tools.resourceproxy.jsspserver.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpRequestUtil;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChainFactory;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpServerHandler implements HttpHandler {

    private static final LogTool  logTool = LogTools.getLogTool(HttpServerHandler.class);

    // @me/hatter/tools/resourceproxy/jsspserver/util/HttpConstants.java
    // public static int STATUS_SUCCESS = 200;
    // public static int STATUS_ERROR = 500;

    private HttpResquestProcessor httpResquestProcessor;

    public HttpServerHandler() {
        this(new DefaultHttpResquestProcessor());
    }

    public HttpServerHandler(HttpResquestProcessor httpResquestProcessor) {
        this.httpResquestProcessor = httpResquestProcessor;
    }

    public HttpServerHandler(ResourceFilterChainFactory resourceFilterChainFactory) {
        this(new FactoryHttpResquestProcessor(resourceFilterChainFactory));
    }

    public HttpServerHandler(List<ResourceFilter> filterList) {
        this(new FactoryHttpResquestProcessor(filterList));
    }

    public void handle(HttpExchange exchange) throws IOException {
        try {
            HttpRequest request = HttpRequestUtil.build(exchange);
            if (logTool.isInfoEnable()) {
                logTool.info("Request: " + request.getMethod() + " " + request.getFullUrl() + " #"
                             + request.getRemoteAddress());
            }

            HttpResponse response = httpResquestProcessor.process(request);
            writeResponse(exchange, response);
        } catch (Throwable t) {
            if ((t instanceof IOException) && StringUtil.contains(t.getMessage(), "Broken pipe")) {
                if (logTool.isInfoEnable()) {
                    logTool.info("Client cancel connection: " + t.getClass().getName() + "@" + t.getMessage());
                }
            } else {
                logTool.error("Exception occured: ", t);
                writeThrowableAndClose(exchange, t);
            }
        }
    }

    public static void writeThrowableAndClose(HttpExchange exchange, Throwable t) {
        writeErrorAndClose(exchange, "Unknow error.\r\n\r\nExcepton: " + StringUtil.printStackTrace(t));
    }

    public static void writeErrorAndClose(HttpExchange exchange, String message) {
        try {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set(ContentTypes.CONTENT_TYPE, ContentTypes.PLAIN_AND_UTF8);
            exchange.sendResponseHeaders(HttpConstants.STATUS_SERVICE_ERROR, 0);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write((message).getBytes(ContentTypes.UTF8_CHARSET));
            responseBody.close();
        } catch (Exception e) {
            logTool.error("Write error to response failed. ", e);
        }
    }

    private void writeResponse(HttpExchange exchange, HttpResponse response) throws UnsupportedEncodingException,
                                                                            IOException {
        byte[] theBytes = response.getBytes();
        if (response.getString() != null) {
            String charset = (response.getCharset() == null) ? ContentTypes.UTF8_CHARSET : response.getCharset();
            theBytes = response.getString().getBytes(charset);
        }

        Headers responseHeaders = exchange.getResponseHeaders();

        for (String key : response.getHeaderMap().keySet()) {
            if (key != null) {
                responseHeaders.put(key, new ArrayList<String>(response.get(key)));
            }
        }

        exchange.sendResponseHeaders(response.getStatus(), 0);

        OutputStream responseBody = exchange.getResponseBody();
        if (theBytes != null) {
            responseBody.write(theBytes);
        }

        responseBody.close();
    }
}
