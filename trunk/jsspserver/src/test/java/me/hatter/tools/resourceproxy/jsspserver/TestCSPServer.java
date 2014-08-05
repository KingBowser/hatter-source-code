package me.hatter.tools.resourceproxy.jsspserver;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.filter.impl.Default404Filter;
import me.hatter.tools.resourceproxy.jsspserver.filter.impl.ListingFileFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.impl.LogFilter;
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

    public static class RedirectFilter implements ResourceFilter {

        public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) throws Exception {
            if (request.getFPath().equals("/redirect")) {
                HttpResponse response = new HttpResponse();
                response.redirect(request.getQueryValue("url"));
                return response;
            } else {
                HttpResponse response = chain.next().filter(request, chain);
                return response;
            }
        }
    }

    public static void main(String[] args) {
        List<ResourceFilter> filters = new ArrayList<ResourceFilter>();
        filters.add(LogFilter.INSTANCE);
        filters.add(new RedirectFilter());
        filters.add(new CSPFilter());
        filters.add(new ListingFileFilter("/Users/hatterjiang/temp"));
        filters.add(Default404Filter.INSTANCE);

        MainHttpServer httpServer = new MainHttpServer(filters, 8000);
        httpServer.run();
    }
}
