package me.hatter.tools.resourceproxy.commons.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

public class ReflectUtil {

    public static Object getFieldValue(Object object, String field, AtomicReference<Class<?>> refType) {
        return getFieldValue(object.getClass(), object, field, refType);
    }

    public static Object getFieldValue(Class<?> clazz, Object object, String field, AtomicReference<Class<?>> refType) {
        Field f = getField(clazz, field);
        if (f == null) {
            throw new RuntimeException("Field " + field + " not found in class " + clazz + ".");
        }
        refType.set(f.getType());
        f.setAccessible(true);
        try {
            return f.get(object);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static ConcurrentMap<Class<?>, ConcurrentMap<String, Field>> fieldClassMap = new ConcurrentHashMap<Class<?>, ConcurrentMap<String, Field>>();

    public static Field getField(Class<?> clazz, String field) {
        if (clazz == null) {
            return null;
        }
        if (clazz == Object.class) {
            return null;
        }
        ConcurrentMap<String, Field> fieldMap = fieldClassMap.get(clazz);
        if (fieldMap != null) {
            if (fieldMap.containsKey(field)) {
                return fieldMap.get(field);
            }
        }
        Field result = internalGetField(clazz, field);
        if (result == null) {
            throw new IllegalStateException("Field: `" + field + "` not found in class: `" + clazz + "`");
        }
        fieldClassMap.putIfAbsent(clazz, new ConcurrentHashMap<String, Field>());
        fieldClassMap.get(clazz).put(field, result);
        return result;
    }

    private static Field internalGetField(Class<?> clazz, String field) {
        if (clazz == null) {
            return null;
        }
        if (clazz == Object.class) {
            return null;
        }
        try {
            return clazz.getDeclaredField(field);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            return getField(clazz.getSuperclass(), field);
        }
    }

    public static List<Field> getFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<Field>();
        getFields(clazz, fieldList);
        return fieldList;
    }

    private static void getFields(Class<?> clazz, List<Field> fieldList) {
        if (clazz == null) {
            return;
        }
        if (clazz == Object.class) {
            return;
        }
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        getFields(clazz.getSuperclass(), fieldList);
    }
}
