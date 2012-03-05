package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class Default404Filter implements ResourceFilter {

    public static final Default404Filter INSTANCE = new Default404Filter();

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        // this is the last filter
        HttpResponse response = new HttpResponse();
        response.setContentType("text/plain");
        response.setCharset("UTF-8");
        response.setStatus(404);
        response.setStatusMessage("Resource not found");
        response.getHeaderMap().set("Content-Type", "text/plain;charset=UTF-8");
        response.setString("Resource not found: " + request.getUri().getPath());
        return response;
    }
}
