package me.hatter.tools.resourceproxy.httpobjects.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    private int                       status;
    private String                    statusMessage;
    private String                    contentType;
    private String                    charset;
    private String                    encoding;
    private Map<String, List<String>> headerMap = new LinkedHashMap<String, List<String>>();
    private byte[]                    bytes;
    private String                    string;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
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

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
