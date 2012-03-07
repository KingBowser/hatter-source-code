package me.hatter.tools.resourceproxy.httpobjects.objects;

import me.hatter.tools.resourceproxy.dbutils.annotation.Field;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;
import me.hatter.tools.resourceproxy.dbutils.annotation.UpdateIgnore;
import me.hatter.tools.resourceproxy.dbutils.util.DBUtil;

@Table
public class HttpObject {

    public static void main(String[] a) {
        System.out.println(DBUtil.generateCreateSQL(HttpObject.class));
    }

    @Field
    @UpdateIgnore
    private Integer id;
    @Field
    private String  method;
    @Field(pk = true)
    private String  url;
    @Field(pk = true)
    private String  accessAddress;
    @Field
    private Integer status;
    @Field
    private String  statusMessage;
    @Field
    private String  contentType;
    @Field
    private String  charset;
    @Field
    private String  encoding;
    @Field
    private String  header;
    @Field
    private String  bytes;
    @Field
    private String  string;
    @Field
    private String  isUpdated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessAddress() {
        return accessAddress;
    }

    public void setAccessAddress(String accessAddress) {
        this.accessAddress = accessAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
    }
}
