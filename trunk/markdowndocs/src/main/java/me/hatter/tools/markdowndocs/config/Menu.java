package me.hatter.tools.markdowndocs.config;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private String     title;
    private String     path;
    private List<Menu> list = new ArrayList<Menu>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Menu> getList() {
        return list;
    }

    public void setList(List<Menu> list) {
        this.list = list;
    }
}
