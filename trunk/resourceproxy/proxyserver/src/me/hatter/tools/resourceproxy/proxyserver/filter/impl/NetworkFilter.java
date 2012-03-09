package me.hatter.tools.resourceproxy.proxyserver.filter.impl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Properties;

import me.hatter.tools.resourceproxy.commons.util.IOUtil;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpObjectUtil;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpResponseUtil;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;

public class NetworkFilter implements ResourceFilter {

    private static Properties HOST_PROPERTIES = new Properties();
    static {
        if (System.getProperties().containsKey("debug")) {
            try {
                HOST_PROPERTIES.load(new FileInputStream("hosts.properties"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static {
        HttpURLConnection.setFollowRedirects(false);
    }

    @Override
    public HttpResponse filter(HttpRequest request, ResourceFilterChain chain) {
        String host = request.getHost();
        if (!request.isLocalHostOrIP()) {
            if (request.isGET() || request.isPOST()) {
                String u = request.getFullUrl();
                try {
                    HttpResponse response = getHttpResponseFromNetwork(request, host, u);
                    response.setFromNetwork(true);
                    return response;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("Method is not supported: " + request.getMethod());
            }
        } else {
            return chain.next().filter(request, chain);
        }
    }

    private HttpResponse getHttpResponseFromNetwork(HttpRequest request, String host, String u)
                                                                                               throws MalformedURLException,
                                                                                               IOException,
                                                                                               ProtocolException {
        HttpResponse response;
        // String realHost = null;
        if (HOST_PROPERTIES.containsKey(host)) {
            // realHost = host;
            u = "http://" + HOST_PROPERTIES.getProperty(host) + request.getUri().toString();
        }
        URL url = new URL(u);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod(request.getMethod());
        System.out.println("[INFO] Request headers for: " + request.getMethod().toUpperCase() + " " + u);
        for (String key : request.getHeaderMap().keySet()) {
            if ("Host".equalsIgnoreCase(key)) {
                System.out.println("\t" + key + ": " + request.get(key).get(0));
                httpURLConnection.setRequestProperty(key, request.get(key).get(0));
            } else {
                for (String value : request.get(key)) {
                    System.out.println("\t" + key + ": " + value);
                    httpURLConnection.addRequestProperty(key, value);
                }
            }
        }
        // if (realHost != null) {
        // httpURLConnection.addRequestProperty("Host", realHost);
        // System.out.println("\tHost: " + realHost);
        // }
        if (request.isPOST() && (request.getPostBytes() != null)) { // if post request, send the data to the server
            httpURLConnection.setDoOutput(true);
            IOUtil.copy(new ByteArrayInputStream(request.getPostBytes()), httpURLConnection.getOutputStream());
        }
        httpURLConnection.connect();

        response = HttpResponseUtil.build(httpURLConnection);

        if (request.isGET()) { // POST cannot proxy, so currently only proxy GET request
            HttpObject httpObject = HttpObjectUtil.frHttpRequest(request, response);
            HttpObject httpObjectFromDB = DataAccessObject.selectObject(httpObject);
            if (httpObjectFromDB == null) {
                System.out.println("[INFO] Http Object from db is null.");
                try {
                    DataAccessObject.insertObject(httpObject);
                } catch (Exception e) {
                    System.out.println("[ERROR] insert data error " + httpObject.getUrl() + " @"
                                       + httpObject.getAccessAddress() + " /" + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                DataAccessObject.updateObject(httpObject);
            }
        }
        return response;
    }
}
