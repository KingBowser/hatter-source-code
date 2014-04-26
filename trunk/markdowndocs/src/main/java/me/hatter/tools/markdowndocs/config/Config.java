package me.hatter.tools.markdowndocs.config;

public class Config {

    private String  name;      // no default value
    private String  title;
    private String  favicon114;
    private String  favicon;
    private String  headTitle;
    private String  imgType;
    private Integer smallSize;
    private Integer bigSize;
    private Double  quality;

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

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public Integer getSmallSize() {
        return smallSize;
    }

    public void setSmallSize(Integer smallSize) {
        this.smallSize = smallSize;
    }

    public Integer getBigSize() {
        return bigSize;
    }

    public void setBigSize(Integer bigSize) {
        this.bigSize = bigSize;
    }

    public Double getQuality() {
        return quality;
    }

    public void setQuality(Double quality) {
        this.quality = quality;
    }
}
