package me.hatter.tools.markdowndocs.config;

public class Config {

    private String name;      // no default value
    private String title;
    private String favicon114;
    private String favicon;
    private String headTitle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFavicon114() {
        return favicon114;
    }

    public void setFavicon114(String favicon114) {
        this.favicon114 = favicon114;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }
}
