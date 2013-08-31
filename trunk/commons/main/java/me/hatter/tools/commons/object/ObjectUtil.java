package me.hatter.tools.commons.object;

public class ObjectUtil {

    public static boolean isNull(Object obj) {
        return (obj == null);
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isClass(Object obj) {
        return isNotNull(obj) && (obj instanceof Class);
    }
}
