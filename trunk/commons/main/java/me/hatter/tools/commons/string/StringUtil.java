package me.hatter.tools.commons.string;

import java.util.List;

public class StringUtil {

    public static final String EMPTY = "";

    public static String trim(String str) {
        return (str == null) ? null : str.trim();
    }

    public static String trimToNull(String str) {
        str = trim(str);
        return ((str == null) || str.isEmpty()) ? null : str;
    }

    public static String trimToEmpty(String str) {
        str = trim(str);
        return (str == null) ? EMPTY : str;
    }

    public static String toUpperCase(String string) {
        return (string == null) ? null : string.toUpperCase();
    }

    public static String toLowerCase(String string) {
        return (string == null) ? null : string.toLowerCase();
    }

    public static String join(List<String> list, String separater) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            sb.append(separater);
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    public static String toUnder(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.charAt(0));
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
            }
            sb.append(c);
        }
        return sb.toString().toLowerCase();
    }

    public static String toCamel(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return EMPTY;
        }
        str = str.toLowerCase();
        StringBuilder sb = new StringBuilder();
        char firstChar = str.charAt(0);
        boolean lastIsUnder = false;
        sb.append(firstChar);
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                lastIsUnder = true;
                continue;
            }
            if (lastIsUnder) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(Character.toLowerCase(c));
            }
            lastIsUnder = false;
        }
        return sb.toString();
    }

    public static final boolean isEmpty(final String str) {
        return ((str == null) || (str.length() == 0));
    }

    public static final boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }

    public static final String notNull(final String str) {
        return (str == null) ? "" : str;
    }

    public static final String upper(final String str) {
        return isEmpty(str) ? str : str.toUpperCase();
    }

    public static final String lower(final String str) {
        return isEmpty(str) ? str : str.toLowerCase();
    }

    public static final boolean equals(final String str0, final String str1) {
        if (str0 == str1) {
            return true;
        }
        return ((str0 != null) && str0.equals(str1));
    }
}
