package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.ContentTypes;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;

public class Default404Filter implements ResourceFilter {

    public static final Default404Filter INSTANCE = new Default404Filter();

    // @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        // this is the last filter
        HttpResponse response = new HttpResponse();
        response.setContentType(ContentTypes.PLAIN_CONTENT_TYPE);
        response.setCharset(ContentTypes.UTF8_CHARSET);
        response.setStatus(HttpConstants.STATUS_NOT_FOUND);
        response.setStatusMessage("Resource not found");
        response.getHeaderMap().set(ContentTypes.CONTENT_TYPE, ContentTypes.PLAIN_AND_UTF8);
        response.setString("Resource not found: " + request.getUri().getPath());
        return response;
    }
}
