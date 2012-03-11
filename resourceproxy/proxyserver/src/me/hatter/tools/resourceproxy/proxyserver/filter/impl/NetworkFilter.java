package me.hatter.tools.resourceproxy.proxyserver.filter.impl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Properties;

import me.hatter.tools.resourceproxy.commons.util.IOUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HostConfig;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpObject;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpRequest;
import me.hatter.tools.resourceproxy.httpobjects.objects.HttpResponse;
import me.hatter.tools.resourceproxy.httpobjects.objects.UserConfig;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpObjectUtil;
import me.hatter.tools.resourceproxy.httpobjects.util.HttpResponseUtil;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilter;
import me.hatter.tools.resourceproxy.jsspserver.filter.ResourceFilterChain;
import me.hatter.tools.resourceproxy.jsspserver.util.HttpConstants;
import me.hatter.tools.resourceproxy.proxyserver.main.ProxyServer;
import sun.net.www.MessageHeader;

public class NetworkFilter implements ResourceFilter {

    private static Properties HOST_PROPERTIES = new Properties();
    static {
        if (System.getProperties().containsKey("debug")) {
            try {
                HOST_PROPERTIES.load(new FileInputStream("hosts.properties"));
            } catch (Exception e) {
                System.out.println("[ERROR] Parse hosts.properties failed. " + StringUtil.printStackTrace(e));
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

    @SuppressWarnings("restriction")
    private HttpResponse getHttpResponseFromNetwork(HttpRequest request, String host, String u)
                                                                                               throws MalformedURLException,
                                                                                               IOException,
                                                                                               ProtocolException {
        HttpResponse response;
        String realHost = null;
        if (HOST_PROPERTIES.containsKey(host)) {
            realHost = host;
            System.out.println("[INFO] Send message to host: " + realHost + " redirected to ip: "
                               + HOST_PROPERTIES.getProperty(host));
            u = "http://" + HOST_PROPERTIES.getProperty(host) + request.getUri().toString();
        } else {
            HostConfig hostConfig = new HostConfig();
            hostConfig.setDomain(host);
            hostConfig.setAccessAddress(request.getIp());
            HostConfig hostConfigFromDB = DataAccessObject.selectObject(hostConfig);
            if (hostConfigFromDB != null) {
                realHost = host;
                System.out.println("[INFO] Send message to host: " + realHost + " redirected to ip: "
                                   + hostConfigFromDB.getTargetIp());
                u = "http://" + hostConfigFromDB.getTargetIp() + request.getUri().toString();
            }
        }
        URL url = new URL(u); // currently only support 80 port
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod(request.getMethod());
        System.out.println("[INFO] Request headers for: " + request.getMethod().toUpperCase() + " " + u);
        for (String key : request.getHeaderMap().keySet()) {
            if (HttpConstants.HEADER_HOST.equalsIgnoreCase(key)) {
                System.out.println("\t" + key + ": " + request.get(key).get(0));
                httpURLConnection.setRequestProperty(key, request.get(key).get(0));
            } else if (HttpConstants.HEADER_USER_AGENT.equalsIgnoreCase(key)) {
                String userAgent = getUserAgent(request);
                if (userAgent == null) {
                    for (String value : request.get(key)) {
                        System.out.println("\t" + key + ": " + value);
                        httpURLConnection.addRequestProperty(key, value);
                    }
                } else {
                    System.out.println("[INFO] Set user agent to: " + userAgent);
                    httpURLConnection.setRequestProperty(HttpConstants.HEADER_USER_AGENT, userAgent);
                }
            } else {
                for (String value : request.get(key)) {
                    System.out.println("\t" + key + ": " + value);
                    httpURLConnection.addRequestProperty(key, value);
                }
            }
        }
        httpURLConnection.setRequestProperty(ProxyServer.HEADER_X_REQUEST_BY, ProxyServer.PROXY_SERVER_VERSION);
        if (realHost != null) {
            try {
                Field fieldOfRequests = httpURLConnection.getClass().getDeclaredField("requests");
                fieldOfRequests.setAccessible(true);
                MessageHeader h = (MessageHeader) fieldOfRequests.get(httpURLConnection);
                h.set(HttpConstants.HEADER_HOST, realHost);
            } catch (Exception e) {
                throw new RuntimeException("Set real host failed: " + realHost, e);
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
        saveOrUpdateResponse(request, response);
        return response;
    }

    private String getUserAgent(HttpRequest request) {
        UserConfig userConfig = new UserConfig();
        userConfig.setAccessAddress(request.getIp());
        UserConfig userConfigFromDB = DataAccessObject.selectObject(userConfig);
        return StringUtil.trimToNull((userConfigFromDB == null) ? null : userConfigFromDB.getUserAgent());
    }

    private void saveOrUpdateResponse(HttpRequest request, HttpResponse response) {
        if (request.isGET()) { // POST cannot proxy, so currently only proxy GET request
            HttpObject httpObject = HttpObjectUtil.frHttpRequest(request, response);
            HttpObject httpObjectFromDB = DataAccessObject.selectObject(httpObject);
            if (httpObjectFromDB == null) {
                System.out.println("[INFO] Http Object from db is null.");
                try {
                    DataAccessObject.insertObject(httpObject);
                } catch (Exception e) {
                    System.out.println("[ERROR] insert data error " + httpObject.getUrl() + " @"
                                       + httpObject.getAccessAddress() + " /" + e.getMessage() + " "
                                       + StringUtil.printStackTrace(e));
                }
            } else {
                DataAccessObject.updateObject(httpObject);
            }
        }
    }
}
