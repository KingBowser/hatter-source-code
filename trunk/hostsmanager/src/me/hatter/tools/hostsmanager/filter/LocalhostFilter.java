package me.hatter.tools.hostsmanager.filter;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class LocalhostFilter implements ResourceFilter {

    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        String host = request.getHost();
        if ("localhost".equals(host.toLowerCase())) {
            return chain.next().filter(request, chain);
        } else if ("127.0.0.1".equals(host)) {
            return chain.next().filter(request, chain);
        }
        throw new RuntimeException("Please use localhost or 127.0.0.1 to access the page.");
    }
}
