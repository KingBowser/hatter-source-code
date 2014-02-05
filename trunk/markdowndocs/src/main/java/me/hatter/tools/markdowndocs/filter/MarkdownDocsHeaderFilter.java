package me.hatter.tools.markdowndocs.filter;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class MarkdownDocsHeaderFilter implements ResourceFilter {

    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        HttpResponse response = chain.next().filter(request, chain);
        if (response != null) {
            response.getHeaderMap().set("Server", "MarkdownDocsServer");
            response.getHeaderMap().set("X-Powered-By", "JSSPServer");
        }
        return response;
    }
}
