package me.hatter.tools.resourceproxy.proxyserver.filter.impl;

import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpObjectUtil;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class DatabaseFilter implements ResourceFilter {

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        if ((!request.isLocalHostOrIP()) && ("GET".equalsIgnoreCase(request.getMethod()))) {
            HttpObject queryHttpObject = new HttpObject();
            queryHttpObject.setUrl(request.getFullUrl());
            queryHttpObject.setAccessAddress(request.getIp());
            HttpObject httpObjectFromDBFirst = DataAccessObject.selectObject(queryHttpObject);
            if (httpObjectFromDBFirst != null) {
                System.out.println("[INFO] Http Object deserilize from db.");
                HttpResponse response = HttpObjectUtil.toHttpRequest(request, httpObjectFromDBFirst);
                response.setFromNetwork(false);
                return response;
            } else {
                System.out.println("[INFO] Http Object from db first is null.");
                return chain.next().filter(request, chain);
            }
        } else {
            return chain.next().filter(request, chain);
        }
    }
}
