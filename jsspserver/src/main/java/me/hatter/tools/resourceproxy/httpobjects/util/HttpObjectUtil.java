package me.hatter.tools.resourceproxy.httpobjects.util;

import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;

public class HttpObjectUtil {

    public static HttpObject frHttpRequest(HttpRequest request, HttpResponse response) {
        HttpObject httpObject = new HttpObject();
        httpObject.setMethod(request.getMethod());
        httpObject.setUrl(request.getFullUrl());
        httpObject.setAccessAddress(request.getIp());
        httpObject.setStatus(response.getStatus());
        httpObject.setStatusMessage(response.getStatusMessage());
        httpObject.setContentType(response.getContentType());
        httpObject.setCharset(response.getCharset());
        httpObject.setEncoding(response.getEncoding());
        httpObject.setHeader(StringUtil.headerToString(response.getHeaderMap()));
        httpObject.setBytes(StringUtil.byteArrayToString(response.getBytes()));
        httpObject.setString(response.getString());

        return httpObject;
    }

    public static HttpResponse toHttpRequest(HttpRequest request, HttpObject httpObject) {
        HttpResponse response = new HttpResponse();
        response.setStatus(httpObject.getStatus());
        response.setStatusMessage(httpObject.getStatusMessage());
        response.setContentType(httpObject.getContentType());
        response.setCharset(httpObject.getCharset());
        response.setEncoding(httpObject.getEncoding());
        response.setHeaderMap(StringUtil.stringToHeader(httpObject.getHeader()));
        response.setBytes(StringUtil.stringToByteArray(httpObject.getBytes()));
        response.setString(httpObject.getString());

        return response;
    }
}
