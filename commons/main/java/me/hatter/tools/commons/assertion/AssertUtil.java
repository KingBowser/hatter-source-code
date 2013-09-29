package me.hatter.tools.commons.assertion;

import java.util.Collection;
import java.util.Map;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.string.StringUtil;

public class AssertUtil {

    public static void isTrue(boolean var) {
        isTrue(var, null);
    }

    public static void isTrue(boolean var, String message) {
        if (!var) {
            throw new AssertionError("Assert fail: " + message);
        }
    }

    public static void isFalse(boolean var) {
        isFalse(var, null);
    }

    public static void isFalse(boolean var, String message) {
        isTrue(!var, message);
    }

    public static void isNull(Object obj) {
        isNull(obj, null);
    }

    public static void isNull(Object obj, String message) {
        isTrue(obj == null, message);
    }

    public static void notNull(Object obj) {
        isNotNull(obj);
    }

    public static void isNotNull(Object obj) {
        isNotNull(obj, null);
    }

    public static void notNull(Object obj, String message) {
        isNotNull(obj, message);
    }

    public static void isNotNull(Object obj, String message) {
        isTrue(obj != null, message);
    }

    public static void isEmpty(String str) {
        isEmpty(str, null);
    }

    public static void isEmpty(String str, String message) {
        isTrue(StringUtil.isEmpty(str), message);
    }

    public static void notEmpty(String str) {
        isNotEmpty(str);
    }

    public static void isNotEmpty(String str) {
        isNotEmpty(str, null);
    }

    public static void notEmpty(String str, String message) {
        isNotEmpty(str);
    }

    public static void isNotEmpty(String str, String message) {
        isTrue(StringUtil.isNotEmpty(str), message);
    }

    public static void isBlank(String str) {
        isBlank(str, null);
    }

    public static void isBlank(String str, String message) {
        isTrue(StringUtil.isBlank(str), message);
    }

    public static void notBlank(String str) {
        isNotBlank(str);
    }

    public static void isNotBlank(String str) {
        isNotBlank(str, null);
    }

    public static void notBlank(String str, String message) {
        isNotBlank(str, message);
    }

    public static void isNotBlank(String str, String message) {
        isTrue(StringUtil.isNotBlank(str), message);
    }

    public static void isEmpty(Collection<?> coll) {
        isEmpty(coll, null);
    }

    public static void isEmpty(Collection<?> coll, String message) {
        isTrue(CollectionUtil.isEmpty(coll), message);
    }

    public static void notEmpty(Collection<?> coll) {
        isNotEmpty(coll);
    }

    public static void isNotEmpty(Collection<?> coll) {
        isNotEmpty(coll, null);
    }

    public static void notEmpty(Collection<?> coll, String message) {
        isNotEmpty(coll, message);
    }

    public static void isNotEmpty(Collection<?> coll, String message) {
        isTrue(CollectionUtil.isNotEmpty(coll), message);
    }

    public static void isEmpty(Map<?, ?> map) {
        isEmpty(map, null);
    }

    public static void isEmpty(Map<?, ?> map, String message) {
        isTrue(((map == null) || map.isEmpty()), message);
    }

    public static void notEmpty(Map<?, ?> map) {
        isNotEmpty(map);
    }

    public static void isNotEmpty(Map<?, ?> map) {
        isNotEmpty(map, null);
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        isNotEmpty(map, message);
    }

    public static void isNotEmpty(Map<?, ?> map, String message) {
        isTrue(((map != null) && (!map.isEmpty())), message);
    }
}
