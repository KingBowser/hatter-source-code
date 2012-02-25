package me.hatter.tools.resourceproxy.dbutils.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectUtil {

    public static Object getFieldValue(Object object, String field) {
        return getFieldValue(object.getClass(), object, field);
    }

    public static Object getFieldValue(Class<?> clazz, Object object, String field) {
        Field f = getField(clazz, field);
        if (f == null) {
            throw new RuntimeException("Field " + field + " not found in class " + clazz + ".");
        }
        f.setAccessible(true);
        try {
            return f.get(object);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class<?> clazz, String field) {
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
