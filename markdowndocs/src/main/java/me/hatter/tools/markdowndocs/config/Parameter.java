package me.hatter.tools.markdowndocs.config;

import java.util.List;

public class Parameter {

    private String       template;
    private List<String> dirs;
    private List<Menu>   lefts;
    private List<Menu>   rights;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }

    public List<Menu> getLefts() {
        return lefts;
    }

    public void setLefts(List<Menu> lefts) {
        this.lefts = lefts;
    }

    public List<Menu> getRights() {
        return rights;
    }

    public void setRights(List<Menu> rights) {
        this.rights = rights;
    }
}
