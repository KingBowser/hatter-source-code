package me.hatter.tools.commons.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectUtil {

    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            return method;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            if (clazz == Object.class) {
                return null;
            } else {
                return getDeclaredMethod(clazz.getSuperclass(), methodName, parameterTypes);
            }
        }
    }

    public static Field getDeclaredField(Class<?> clazz, String field) {
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
            return getDeclaredField(clazz.getSuperclass(), field);
        }
    }

    public static List<Field> getDeclaredFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<Field>();
        getDeclaredFields(clazz, fieldList);
        return fieldList;
    }

    private static void getDeclaredFields(Class<?> clazz, List<Field> fieldList) {
        if (clazz == null) {
            return;
        }
        if (clazz == Object.class) {
            return;
        }
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        getDeclaredFields(clazz.getSuperclass(), fieldList);
    }
}
