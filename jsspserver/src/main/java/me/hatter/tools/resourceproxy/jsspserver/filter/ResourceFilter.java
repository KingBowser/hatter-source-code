package me.hatter.tools.resourceproxy.jsspserver.filter;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;

public interface ResourceFilter {

    HttpResponse filter(HttpRequest request, ResourceFilterChain chain) throws Exception;
}
