package me.hatter.tools.resourceproxy.jsspserver.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;

public class DefaultResourceFilterChain implements ResourceFilterChain {

    private static List<ResourceFilter> filterList = new ArrayList<ResourceFilter>();
    static {
        for (ResourceFilter resourceFilter : ServiceLoader.load(ResourceFilter.class)) {
            filterList.add(resourceFilter);
        }
    }
    private int                         index      = 0;

    public ResourceFilter next() {
        return (index >= filterList.size()) ? null : filterList.get(index++);
    };

    public static HttpResponse filterChain(HttpRequest request) {
        ResourceFilterChain chain = new DefaultResourceFilterChain();
        return chain.next().filter(request, chain);
    }
}
