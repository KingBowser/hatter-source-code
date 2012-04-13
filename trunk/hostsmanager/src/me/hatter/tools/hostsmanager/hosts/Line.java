package me.hatter.tools.hostsmanager.hosts;

public class Line {

    public static final String COMMENT      = "#";
    public static final String GROUP        = "#Group";
    public static final String GROUP_START  = "#GroupStart";
    public static final String GROUP_END    = "#GroupEnd";

    private boolean            isComment    = false;
    private boolean            isGroupStart = false;
    private boolean            isGroupEnd   = false;
    private String             line;
    private String             group;

    public static Line parse(String line) {
        Line l = new Line();
        l.line = line;
        if (line.startsWith(COMMENT)) {
            l.isComment = true;
        }
        if (line.startsWith(GROUP_START)) {
            l.isGroupStart = true;
            l.group = line.substring(GROUP_START.length()).trim();
        }
        if (line.startsWith(GROUP_END)) {
            l.isGroupEnd = true;
        }
        return l;
    }

    public boolean isComment() {
        return isComment;
    }

    public void setComment(boolean isComment) {
        this.isComment = isComment;
    }

    public boolean isGroupStart() {
        return isGroupStart;
    }

    public void setGroupStart(boolean isGroupStart) {
        this.isGroupStart = isGroupStart;
    }

    public boolean isGroupEnd() {
        return isGroupEnd;
    }

    public void setGroupEnd(boolean isGroupEnd) {
        this.isGroupEnd = isGroupEnd;
    }

    public boolean isGroup() {
        return (isGroupStart || isGroupEnd);
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
