package me.hatter.tools.commons.regex;

import java.util.regex.Pattern;

public class RegexUtil {

    public static Pattern createPattern(String pattern, boolean ignoreCase) {
        if (pattern == null) {
            return null;
        }
        pattern = pattern.startsWith("^") ? pattern : (".*" + pattern);
        pattern = pattern.endsWith("$") ? pattern : (pattern + ".*");
        return ignoreCase ? Pattern.compile(pattern, Pattern.CASE_INSENSITIVE) : Pattern.compile(pattern);
    }
}
