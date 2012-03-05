package me.hatter.tools.resourceproxy.proxyserver.filter.impl;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class RootPathFilter implements ResourceFilter {

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        String fpath = request.getFPath();
        if ("/".equals(fpath)) {
            System.out.println("[INFO] Redirect: index.jssp?jsspaction=resourceproxy.Index");
            HttpResponse response = new HttpResponse();
            response.redirect("index.jssp?jsspaction=resourceproxy.Index");
            return response;
        } else {
            return chain.next().filter(request, chain);
        }
    }
}
