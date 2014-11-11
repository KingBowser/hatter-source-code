package me.hatter.tools.commons.net.mail.header;

public class ReceivedFrom {

    private String host;
    private String reservedHost;
    private String ip;
    private String message;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getReservedHost() {
        return reservedHost;
    }

    public void setReservedHost(String reservedHost) {
        this.reservedHost = reservedHost;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
