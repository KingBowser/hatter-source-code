package me.hatter.tools.commons.number;

public class IntegerUtil {

    public static boolean isNull(Integer integer) {
        return (integer == null);
    }

    public static boolean isNotNull(Integer integer) {
        return (integer != null);
    }

    public static int nullAsZero(Integer integer) {
        return (integer == null) ? 0 : integer.intValue();
    }

    public static Integer tryParse(String str) {
        try {
            return Integer.valueOf(Integer.parseInt(str));
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static int tryParse(String str, int defaultVal) {
        Integer val = tryParse(str);
        return (val == null) ? defaultVal : val.intValue();
    }
}
