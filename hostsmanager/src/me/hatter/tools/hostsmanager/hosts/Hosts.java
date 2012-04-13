package me.hatter.tools.hostsmanager.hosts;

import java.util.ArrayList;
import java.util.List;

public class Hosts {

    private List<Group> groups = new ArrayList<Group>();

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public static Hosts parse(List<String> lines) {
        Hosts hosts = new Hosts();
        List<Line> ll = parseLines(lines);
        Group group = new Group();
        for (Line l : ll) {
            if (l.isGroupStart()) {
                if (!group.getLines().isEmpty()) {
                    hosts.getGroups().add(group);
                    group = new Group();
                }
                group.setGroup(l.getGroup());
                group.getLines().add(l);
            } else {
                group.getLines().add(l);
                if (l.isGroupEnd()) {
                    hosts.getGroups().add(group);
                    group = new Group();
                }
            }
        }
        if (!group.getLines().isEmpty()) {
            hosts.getGroups().add(group);
        }
        return hosts;
    }

    public List<String> toLines() {
        List<String> lines = new ArrayList<String>();
        for (Group g : getGroups()) {
            if (g.getGroup() == null) {
                for (Line l : g.getLines()) {
                    lines.add(l.getLine());
                }
            } else {
                lines.add(Line.GROUP_START + " " + g.getGroup());
                for (Line l : g.getLines()) {
                    if (!l.isGroup()) {
                        lines.add(l.getLine());
                    }
                }
                lines.add(Line.GROUP_END);
            }
        }
        return lines;
    }

    private static List<Line> parseLines(List<String> lines) {
        List<Line> result = new ArrayList<Line>();
        for (String l : lines) {
            result.add(Line.parse(l));
        }
        return result;
    }
}
