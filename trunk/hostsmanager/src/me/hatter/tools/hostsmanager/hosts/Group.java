package me.hatter.tools.hostsmanager.hosts;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private String     group;
    private List<Line> lines = new ArrayList<Line>();

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
