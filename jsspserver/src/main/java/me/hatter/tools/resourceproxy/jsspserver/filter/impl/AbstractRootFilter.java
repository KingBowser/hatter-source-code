package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public abstract class AbstractRootFilter implements ResourceFilter {

    private static final LogTool logTool   = LogTools.getLogTool(AbstractRootFilter.class);

    public static final String   ROOT_PATH = "/";

    // @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) throws Exception {
        if (ROOT_PATH.equals(request.getFPath())) {
            if (logTool.isInfoEnable()) {
                logTool.info("Redirect for root: " + homePath());
            }
            HttpResponse response = new HttpResponse();
            response.redirect(homePath());
            return response;
        } else {
            return chain.next().filter(request, chain);
        }
    }

    abstract protected String homePath();
}
