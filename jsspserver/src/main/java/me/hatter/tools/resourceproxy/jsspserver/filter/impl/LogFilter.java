package me.hatter.tools.resourceproxy.jsspserver.filter.impl;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class LogFilter implements ResourceFilter {

    public static final LogFilter INSTANCE = new LogFilter();

    private static final LogTool  log      = LogTools.getLogTool(LogFilter.class);

    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("Method: " + request.getMethod());
        list.add("FPath: " + request.getFPath());
        list.add("RemoteAddr: " + request.getRemoteAddress());
        list.add("QueryMap: " + request.getQueryMap());
        list.add("PostMap: " + request.getPostMap());

        log.info("Query: \n       " + StringUtil.join(list, "\n       ") + "\n" + StringUtil.repeat("-", 80));

        return chain.next().filter(request, chain);
    }
}
