package me.hatter.tools.finding;

import java.util.regex.Pattern;


public class RegexMatcher implements Matcher {

    private Matcher parent;
    private Pattern search;

    public RegexMatcher(Matcher parent, String regex, boolean ignoreCase) {
        this.parent = parent;
        regex = (regex.startsWith("^")) ? regex : (".*" + regex);
        regex = (regex.endsWith("$")) ? regex : (regex + ".*");
        if (ignoreCase) {
            this.search = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        } else {
            this.search = Pattern.compile(regex);
        }
    }

    public boolean match(String line) {
        if (line == null) return false;
        if ((parent != null) && (!parent.match(line))) return false;
        return search.matcher(line).matches();
    }
}
