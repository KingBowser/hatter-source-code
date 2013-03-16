package me.hatter.tools.rssreader.entity;

import me.hatter.tools.resourceproxy.dbutils.annotation.Field;
import me.hatter.tools.resourceproxy.dbutils.annotation.Table;

@Table(name = "rss_url", defaultAllFields = true)
public class RssUrl {

    @Field(pk = true)
    private Integer id;
    private String  url;
    private String  description;
    private String  isValid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}
