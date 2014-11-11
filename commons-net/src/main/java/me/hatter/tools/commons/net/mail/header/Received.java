package me.hatter.tools.commons.net.mail.header;

import java.util.Date;

public class Received {

    private ReceivedFrom from;
    private String       by;
    private String       over;
    private String       sfor;
    private String       with;
    private String       id;
    private Date         time;

    public ReceivedFrom getFrom() {
        return from;
    }

    public void setFrom(ReceivedFrom from) {
        this.from = from;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getSfor() {
        return sfor;
    }

    public void setSfor(String sfor) {
        this.sfor = sfor;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
