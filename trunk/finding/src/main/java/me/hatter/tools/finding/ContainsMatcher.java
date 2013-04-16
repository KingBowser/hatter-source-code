package me.hatter.tools.finding;


public class ContainsMatcher implements Matcher {

    private Matcher parent;
    private String  search;
    private boolean ignoreCase;

    public ContainsMatcher(Matcher parent, String search, boolean ignoreCase) {
        this.parent = parent;
        this.search = ignoreCase ? search.toLowerCase() : search;
        this.ignoreCase = ignoreCase;
    }

    public boolean match(String line) {
        if (line == null) return false;
        if ((parent != null) && (!parent.match(line))) return false;
        return (ignoreCase) ? line.toLowerCase().contains(search) : line.contains(search);
    }
}
