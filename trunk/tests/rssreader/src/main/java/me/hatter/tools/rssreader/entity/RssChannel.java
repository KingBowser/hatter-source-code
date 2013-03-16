package me.hatter.tools.rssreader.entity;

import java.util.Date;

import me.hatter.tools.resourceproxy.dbutils.annotation.Field;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;

@Table(name = "rss_channel", defaultAllFields = true)
public class RssChannel {

    @Field(pk = true)
    private Integer id;
    private String  title;
    private String  link;
    private String  description;
    private Date    lastBuildDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(Date lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }
}
