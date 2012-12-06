package me.hatter.tools.commons.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectUtil {

    private static final Map<String, Boolean> classPresentMap = new HashMap<String, Boolean>();

    public static boolean isClassPresent(String className) {
        synchronized (classPresentMap) {
            if (classPresentMap.get(className) != null) {
                return classPresentMap.get(className).booleanValue();
            }
        }
        boolean found = true;
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            found = false;
        }
        synchronized (classPresentMap) {
            classPresentMap.put(className, Boolean.valueOf(found));
        }
        return found;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> interf) {
        Class<?> clazz;
        boolean isClazz = (obj instanceof Class);
        if (obj instanceof Class) {
            clazz = (Class<?>) obj;
        } else {
            clazz = obj.getClass();
        }
        final Object fObj = obj;
        final Class<?> fClazz = clazz;
        final boolean fIsClazz = isClazz;

        return (T) Proxy.newProxyInstance(interf.getClassLoader(), new Class<?>[] { interf }, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Method m = getDeclaredMethod(fClazz, method.getName(), method.getParameterTypes());
                if (m == null) {
                    throw new RuntimeException("Method '" + fClazz.getName() + "." + method.getName() + "("
                                               + method.getParameterTypes() + ")' not found.");
                }
                if (!m.isAccessible()) m.setAccessible(true);
                return m.invoke((fIsClazz ? null : fObj), args);
            }
        });
    }

    public static Class<?> classForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

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
