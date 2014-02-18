package me.hatter.tools.resourceproxy.jsspserver.handler;

import java.util.List;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.DefaultResourceFilterChainFactory;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChainFactory;

public class FactoryHttpResquestProcessor implements HttpResquestProcessor {

    private ResourceFilterChainFactory resourceFilterChainFactory;

    public FactoryHttpResquestProcessor(ResourceFilterChainFactory resourceFilterChainFactory) {
        this.resourceFilterChainFactory = resourceFilterChainFactory;
    }

    public FactoryHttpResquestProcessor(List<ResourceFilter> filterList) {
        this(new DefaultResourceFilterChainFactory(filterList));
    }

    public HttpResponse process(HttpRequest request) throws Exception {
        ResourceFilterChain chain = resourceFilterChainFactory.makeChain();
        return chain.next().filter(request, chain);
    }
}
