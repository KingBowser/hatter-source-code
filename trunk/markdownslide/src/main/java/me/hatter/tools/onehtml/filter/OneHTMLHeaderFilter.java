package me.hatter.tools.onehtml.filter;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class OneHTMLHeaderFilter implements ResourceFilter {

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) throws Exception {
        HttpResponse response = chain.next().filter(request, chain);
        if (response != null) {
            response.getHeaderMap().set("Server", "OneHTMLServer");
            response.getHeaderMap().set("X-Powered-By", "JSSPServer");
        }
        return response;
    }
}
