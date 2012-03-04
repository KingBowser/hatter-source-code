package me.hatter.tools.resourceproxy.httpobjects.objects;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.hatter.tools.resourceproxy.commons.util.KeyValueListMap;

public class HttpRequest {

    private int               uploadCount;
    private String            method;
    private String            host;
    private URI               uri;
    private String            fullUrl;
    private InetSocketAddress remoteAddress;
    private KeyValueListMap   headerMap     = new KeyValueListMap();

    private KeyValueListMap   queryValueMap = new KeyValueListMap();
    private KeyValueListMap   queryMap      = new KeyValueListMap();
    private KeyValueListMap   postMap       = new KeyValueListMap();

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

    public String getHost() {
        return host;
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

    public KeyValueListMap getHeaderMap() {
        return headerMap;
    }

    public void set(String key, Collection<String> values) {
        List<String> valueList = headerMap.get(key);
        if (valueList == null) {
            valueList = new ArrayList<String>();
            headerMap.put(key, valueList);
        }
        valueList.addAll(values);
        if ("Host".equalsIgnoreCase(key)) {
            this.host = valueList.get(0);
        }
    }

    public List<String> get(String key) {
        return headerMap.get(key);
    }

    public KeyValueListMap getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(KeyValueListMap queryMap) {
        this.queryMap = queryMap;
    }

    public KeyValueListMap getPostMap() {
        return postMap;
    }

    public KeyValueListMap getQueryValueMap() {
        return queryValueMap;
    }

    public void setQueryValueMap(KeyValueListMap queryValueMap) {
        this.queryValueMap = queryValueMap;
    }

    public void setPostMap(KeyValueListMap postMap) {
        this.postMap = postMap;
    }

    public String getQueryValue(String key) {
        List<String> list = getQueryValueList(key);
        return ((list == null) || list.isEmpty()) ? null : list.get(0);
    }

    public List<String> getQueryValueList(String key) {
        List<String> list1 = getQueryMap().get(key);
        List<String> list2 = getPostMap().get(key);
        if ((list1 == null) && (list2 == null)) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        if (list1 != null) {
            list.addAll(list1);
        }
        if (list2 != null) {
            list.addAll(list2);
        }
        return list;
    }
}
