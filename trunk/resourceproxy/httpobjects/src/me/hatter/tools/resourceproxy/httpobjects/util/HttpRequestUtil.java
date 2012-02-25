package me.hatter.tools.resourceproxy.httpobjects.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class HttpRequestUtil {

    private static Set<String> IGNORE_HEADER_SET = new HashSet<String>(Arrays.asList("If-modified-since", // ,
                                                                                     "If-none-match"));

    @SuppressWarnings("restriction")
    public static HttpRequest build(HttpExchange exchange) {
        HttpRequest request = new HttpRequest();
        request.setMethod(exchange.getRequestMethod().toUpperCase());
        request.setUri(exchange.getRequestURI());
        request.setRemoteAddress(exchange.getRemoteAddress());
        Headers requestHeaders = exchange.getRequestHeaders();
        for (String key : requestHeaders.keySet()) {
            if (!IGNORE_HEADER_SET.contains(key)) {
                List<String> values = requestHeaders.get(key);
                request.set(key, values);
            }
        }
        request.setHost(request.getHost());
        return request;
    }
}
