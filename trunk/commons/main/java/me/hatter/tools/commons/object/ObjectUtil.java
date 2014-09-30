package me.hatter.tools.commons.object;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectUtil {

    public static boolean isNull(Object obj) {
        return (obj == null);
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isArray(Object obj) {
        return isNotNull(obj) && obj.getClass().isArray();
    }

    public static boolean isPrimitive(Object obj) {
        return isNotNull(obj) && obj.getClass().isPrimitive();
    }

    public static boolean isEnum(Object obj) {
        return isNotNull(obj) && obj.getClass().isEnum();
    }

    public static boolean isCollection(Object obj) {
        return isNotNull(obj) && (obj instanceof Collection);
    }

    public static boolean isSet(Object obj) {
        return isNotNull(obj) && (obj instanceof Set);
    }

    public static boolean isList(Object obj) {
        return isNotNull(obj) && (obj instanceof List);
    }

    public static boolean isMap(Object obj) {
        return isNotNull(obj) && (obj instanceof Map);
    }

    public static boolean isClass(Object obj) {
        return isNotNull(obj) && (obj instanceof Class);
    }

    public static String toString(Object obj) {
        return toString(obj, null);
    }

    public static String toString(Object obj, String def) {
        return (obj == null) ? def : obj.toString();
    }

    public static String objectToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
    }
}
