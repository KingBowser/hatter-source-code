package me.hatter.tools.commons.object;

import java.util.Collection;
import java.util.Map;

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

    public static boolean isCollection(Object obj) {
        return isNotNull(obj) && (obj instanceof Collection);
    }

    public static boolean isMap(Object obj) {
        return isNotNull(obj) && (obj instanceof Map);
    }

    public static boolean isClass(Object obj) {
        return isNotNull(obj) && (obj instanceof Class);
    }
}
