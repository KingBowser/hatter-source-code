package me.hatter.tools.resourceproxy.jsspserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.filter.impl.Default404Filter;
import me.hatter.tools.resourceproxy.jsspserver.filter.impl.ListingFileFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.impl.LogFilter;
import me.hatter.tools.resourceproxy.jsspserver.handler.HttpServerHandler;
import me.hatter.tools.resourceproxy.jsspserver.main.MainHttpServer;

public class TestCSPServer {

    public static class CSPFilter implements ResourceFilter {

        public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) throws Exception {
            HttpResponse response = chain.next().filter(request, chain);
            boolean isReport = true;
            String header = isReport ? "content-security-policy-report-only" : "content-security-policy";
            response.getHeaderMap().set(header,
                                        "img-src 'self' http://*.aliimg.com;script-src 'self' http://*.aliimg.com;report-uri http://127.0.0.1:8000/");
            return response;
        }
    }

    public static void main(String[] args) {
        List<ResourceFilter> filters = new ArrayList<ResourceFilter>();
        filters.add(new LogFilter());
        filters.add(new CSPFilter());
        filters.add(new ListingFileFilter("/Users/hatterjiang/temp/csp"));
        filters.add(new Default404Filter());

        MainHttpServer httpServer = new MainHttpServer(new HttpServerHandler(filters), Arrays.asList(8000));
        httpServer.run();
    }
}