package me.hatter.tools.resourceproxy.httpobjects.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.hatter.tools.resourceproxy.commons.util.KeyValueListMap;

public class HttpResponse {

    private boolean         isFromNetwork;
    private int             status;
    private String          statusMessage;
    private String          contentType;
    private String          charset;
    private String          encoding;
    private KeyValueListMap headerMap = new KeyValueListMap();
    private byte[]          bytes;
    private String          string;
    private boolean         isFinish  = false;

    public boolean isFromNetwork() {
        return isFromNetwork;
    }

    public void setFromNetwork(boolean isFromNetwork) {
        this.isFromNetwork = isFromNetwork;
    }

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

    public void setHeaderMap(KeyValueListMap headerMap) {
        this.headerMap = headerMap;
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

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public boolean isRedirect() {
        return ((getStatus() == 301) || (getStatus() == 302));
    }

    public void redirect(String url) {
        this.setStatus(302);
        this.setStatusMessage("OK");
        this.headerMap.set("Location", url);
    }
}
