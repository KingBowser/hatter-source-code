package me.hatter.tools.resourceproxy.httpobjects.objects;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private String                    method;
    private URI                       uri;
    private InetSocketAddress         remoteAddress;
    private Map<String, List<String>> headerMap = new LinkedHashMap<String, List<String>>();

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
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
