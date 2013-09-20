package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class GZipFilter implements ResourceFilter {

    // @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        HttpResponse response = chain.next().filter(request, chain);
        // TODO check is support gzip and then do gzip
        return response;
    }
}
