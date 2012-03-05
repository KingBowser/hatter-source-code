package me.hatter.tools.resourceproxy.proxyserver.filter.impl;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class HostCheckFilter implements ResourceFilter {

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        if (request.getHost() == null) {
            System.out.println("No host assigned: " + request.getUri().toString());
            throw new RuntimeException("No host assigned: " + request.getUri().toString());
        } else {
            return chain.next().filter(request, chain);
        }
    }
}
