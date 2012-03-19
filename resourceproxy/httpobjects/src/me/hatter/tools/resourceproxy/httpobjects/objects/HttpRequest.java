package me.hatter.tools.resourceproxy.httpobjects.objects;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.hatter.tools.resourceproxy.commons.util.KeyValueListMap;

public class HttpRequest {

    private static Set<String> THIS_HOST_SET = new HashSet<String>();
    static {
        String th = System.getProperty("thishost");
        THIS_HOST_SET.add("localhost");
        if (th != null) {
            for (String h : th.split(",")) {
                THIS_HOST_SET.add(h.toLowerCase());
            }
        }
    }

    private int                uploadCount;
    private String             method;
    private String             host;
    private Integer            port;
    private URI                uri;
    private String             fullUrl;
    private InetSocketAddress  remoteAddress;
    private KeyValueListMap    headerMap     = new KeyValueListMap();
    private byte[]             postBytes;

    private KeyValueListMap    queryValueMap = new KeyValueListMap();
    private KeyValueListMap    queryMap      = new KeyValueListMap();
    private KeyValueListMap    postMap       = new KeyValueListMap();

    public boolean isGET() {
        return "GET".equalsIgnoreCase(method);
    }

    public boolean isPOST() {
        return "POST".equalsIgnoreCase(method);
    }

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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public boolean isLocalHostOrIP() {
        return (THIS_HOST_SET.contains(getHost().toLowerCase()) || getHost().matches("\\d+(\\.\\d+){3}(:\\d+)?"));
    }

    public String getFPath() {
        String path = this.uri.getPath();
        int indexOfQ = path.indexOf('?');
        if (indexOfQ < 0) {
            return path;
        } else {
            return path.substring(0, indexOfQ);
        }
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
            String theHost = valueList.get(0);
            if (theHost.indexOf(':') >= 0) {
                this.host = theHost.substring(0, theHost.indexOf(':'));
                this.port = new Integer(theHost.substring(theHost.indexOf(':') + 1));
            } else {
                this.host = theHost;
            }
        }
    }

    public List<String> get(String key) {
        return headerMap.get(key);
    }

    public byte[] getPostBytes() {
        return postBytes;
    }

    public void setPostBytes(byte[] postBytes) {
        this.postBytes = postBytes;
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
