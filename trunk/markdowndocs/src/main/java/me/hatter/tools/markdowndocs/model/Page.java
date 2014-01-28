package me.hatter.tools.markdowndocs.model;

import java.util.List;

public class Page {

    private String         path;
    private String         headerCode;
    private List<MenuItem> lefts;
    private List<MenuItem> rights;
    private String         summary;
    private String         notice;
    private String         footer;
    private String         index;
    private List<Section>  sections;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHeaderCode() {
        return headerCode;
    }

    public void setHeaderCode(String headerCode) {
        this.headerCode = headerCode;
    }

    public List<MenuItem> getLefts() {
        return lefts;
    }

    public void setLefts(List<MenuItem> lefts) {
        this.lefts = lefts;
    }

    public List<MenuItem> getRights() {
        return rights;
    }

    public void setRights(List<MenuItem> rights) {
        this.rights = rights;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
