package me.hatter.tools.commons.string;

import java.util.Collection;

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

    public static String join(Object[] objects, String separator) {
        if (objects == null) {
            return null;
        }
        if (objects.length == 0) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(objects[0]);
        for (int i = 1; i < objects.length; i++) {
            sb.append(separator).append(objects[i]);
        }
        return sb.toString();
    }

    public static String join(Collection<String> collection, String separator) {
        if (collection == null) {
            return null;
        }
        if (collection.size() == 0) {
            return EMPTY;
        }
        return join(collection.toArray(new String[collection.size()]), separator);
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

    public static boolean isNull(String str) {
        return (str == null);
    }

    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    public static boolean isEmpty(final String str) {
        return ((str == null) || (str.length() == 0));
    }

    public static boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(final String str) {
        return ((str == null) || (str.trim().length() == 0));
    }

    public static boolean isNotBlank(final String str) {
        return !isBlank(str);
    }

    public static String notNull(final String str) {
        return (str == null) ? EMPTY : str;
    }

    public static String upper(final String str) {
        return isEmpty(str) ? str : str.toUpperCase();
    }

    public static String lower(final String str) {
        return isEmpty(str) ? str : str.toLowerCase();
    }

    public static boolean equals(final String str0, final String str1) {
        if (str0 == str1) {
            return true;
        }
        return ((str0 != null) && str0.equals(str1));
    }

    public static String repeat(String s, int count) {
        if (count <= 0) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder(notNull(s).length() * count + 1);
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String paddingZeroLeft(String str, int len) {
        return padding(str, len, '0', true);
    }

    public static String paddingSpaceLeft(String str, int len) {
        return padding(str, len, ' ', true);
    }

    public static String paddingSpaceRight(String str, int len) {
        return padding(str, len, ' ', false);
    }

    public static String padding(String str, int len, char c, boolean isLeft) {
        String s = notNull(str);
        String rc = repeat(new String(new char[] { c }), (len - s.length()));
        return (isLeft ? rc + s : s + rc);
    }

    public static boolean contains(String str, String substr) {
        if (str == null) {
            return false;
        }
        if (substr == null) {
            return true;
        }
        return str.contains(str);
    }

    public static boolean containsAny(String str, String substr, String... substrs) {
        if (str == null) {
            return false;
        }
        if (substr == null) {
            return true;
        }
        if (str.contains(str)) {
            return true;
        }
        if (substrs != null) {
            for (String ss : substrs) {
                if (str.contains(ss)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsAll(String str, String substr, String... substrs) {
        if (str == null) {
            return false;
        }
        if (!str.contains(str)) {
            return false;
        }
        if (substrs != null) {
            for (String ss : substrs) {
                if (!str.contains(ss)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean containsIgnoreCase(String str, String substr) {
        return contains(toLowerCase(str), toLowerCase(substr));
    }

    public static String substringBefore(String str, String split) {
        if ((split == null) || (split.length() == 0) || (str == null) || (str.length() == 0)) {
            return str;
        }
        if (str.length() < split.length()) {
            return null;
        }
        int indexOfSplit = str.indexOf(split);
        if (indexOfSplit < 0) {
            return null;
        }
        return str.substring(0, indexOfSplit);
    }

    public static String substringAfter(String str, String split) {
        if ((split == null) || (split.length() == 0) || (str == null) || (str.length() == 0)) {
            return str;
        }
        if (str.length() < split.length()) {
            return null;
        }
        int indexOfSplit = str.indexOf(split);
        if (indexOfSplit < 0) {
            return null;
        }
        return str.substring(indexOfSplit + split.length());
    }

    public static String substringBeforeLast(String str, String split) {
        if ((split == null) || (split.length() == 0) || (str == null) || (str.length() == 0)) {
            return str;
        }
        if (str.length() < split.length()) {
            return null;
        }
        int indexOfSplit = str.lastIndexOf(split);
        if (indexOfSplit < 0) {
            return null;
        }
        return str.substring(0, indexOfSplit);
    }

    public static String substringAfterLast(String str, String split) {
        if ((split == null) || (split.length() == 0) || (str == null) || (str.length() == 0)) {
            return str;
        }
        if (str.length() < split.length()) {
            return null;
        }
        int indexOfSplit = str.lastIndexOf(split);
        if (indexOfSplit < 0) {
            return null;
        }
        return str.substring(indexOfSplit + split.length());
    }
}
