package me.hatter.tools.resourceproxy.jsspserver.filter;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;

public interface ResourceFilter {

    void filter(HttpRequest request, HttpResponse response, ResourceFilterChain chain);
}
