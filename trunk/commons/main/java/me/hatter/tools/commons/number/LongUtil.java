package me.hatter.tools.commons.number;

public class LongUtil {

    public static boolean isNull(Long longv) {
        return (longv == null);
    }

    public static boolean isNotNull(Long longv) {
        return (longv != null);
    }

    public static long nullAsZero(Long longv) {
        return (longv == null) ? 0L : longv.longValue();
    }

    public static Long tryParse(String str) {
        try {
            return Long.valueOf(Long.parseLong(str));
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public static long tryParse(String str, long defaultVal) {
        Long val = tryParse(str);
        return (val == null) ? defaultVal : val.longValue();
    }
}
