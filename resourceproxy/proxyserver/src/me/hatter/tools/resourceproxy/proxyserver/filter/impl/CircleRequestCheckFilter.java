package me.hatter.tools.resourceproxy.proxyserver.filter.impl;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.proxyserver.main.ProxyServer;

public class CircleRequestCheckFilter implements ResourceFilter {

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        if (request.getHeaderMap().getFirstIgnoreCase(ProxyServer.HEADER_X_REQUEST_BY) != null) {
            System.out.println("Cirecle request found: " + request.getUri().toString());
            throw new RuntimeException("Cirecle request found: " + request.getUri().toString());
        } else {
            return chain.next().filter(request, chain);
        }
    }
}
