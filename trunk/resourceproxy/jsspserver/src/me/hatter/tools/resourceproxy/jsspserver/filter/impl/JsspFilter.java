package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import java.io.File;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.server.JsspServer;

public class JsspFilter implements ResourceFilter {

    private static final String HTML_CONTENT_TYE = "application/octet-stream";

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        String fpath = request.getFPath();
        if (fpath.toLowerCase().endsWith(".jssp")) {
            File tfile = new File(JsspServer.JSSP_PATH, fpath);
            if (tfile.exists()) {
                // TODO
                return null;
            } else {
                return Default404Filter.INSTANCE.filter(request, chain);
            }
        } else {
            return chain.next().filter(request, chain);
        }
    }
}
