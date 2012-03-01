package me.hatter.tools.resourceproxy.httpobjects.objects;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private int                       uploadCount;
    private String                    method;
    private String                    host;
    private URI                       uri;
    private String                    fullUrl;
    private InetSocketAddress         remoteAddress;
    private Map<String, List<String>> headerMap = new LinkedHashMap<String, List<String>>();

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getIp() {
        String ip = this.getRemoteAddress().getAddress().toString();
        return ip.startsWith("/") ? ip.substring(1) : ip;
    }

    public Map<String, List<String>> getHeaderMap() {
        return headerMap;
    }

    public void set(String key, Collection<String> values) {
        List<String> valueList = headerMap.get(key);
        if (valueList == null) {
            valueList = new ArrayList<String>();
            headerMap.put(key, valueList);
        }
        valueList.addAll(values);
    }

    public List<String> get(String key) {
        return headerMap.get(key);
    }

    public String getFirst(String key) {
        List<String> valueList = headerMap.get(key);
        return ((valueList == null) || (valueList.size() == 0)) ? null : valueList.get(0);
    }

    public String getHost() {
        return getFirst("Host");
    }
}
