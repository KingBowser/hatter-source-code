package me.hatter.tools.markdownslide.config;

public class Config {

    private String  template;
    private String  favicon;
    private String  title;
    private String  description;
    private String  keywords;
    private boolean isOneHtml = false; // XXX

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean isOneHtml() {
        return isOneHtml;
    }

    public void setOneHtml(boolean isOneHtml) {
        this.isOneHtml = isOneHtml;
    }
}
