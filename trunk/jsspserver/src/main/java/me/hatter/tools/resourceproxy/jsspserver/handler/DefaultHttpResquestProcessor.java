package me.hatter.tools.resourceproxy.jsspserver.handler;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.DefaultResourceFilterChain;

public class DefaultHttpResquestProcessor implements HttpResquestProcessor {

    public HttpResponse process(HttpRequest request) throws Exception {
        return DefaultResourceFilterChain.filterChain(request);
    }
}
