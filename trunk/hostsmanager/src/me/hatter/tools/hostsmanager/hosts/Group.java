package me.hatter.tools.hostsmanager.hosts;

import java.io.PrintWriter;
import java.io.StringWriter;
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

    public String getJoinedLines() {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            List<String> ls = new ArrayList<String>();
            for (Line l : getLines()) {
                if (!l.isGroup()) {
                    ls.add(l.getLine());
                }
            }
            for (int i = 0; i < ls.size(); i++) {
                if (i < (ls.size() - 1)) {
                    pw.println(ls.get(i));
                } else {
                    pw.print(ls.get(i));
                }
            }
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
