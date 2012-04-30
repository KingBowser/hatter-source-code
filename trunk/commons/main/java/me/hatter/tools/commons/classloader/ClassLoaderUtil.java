package me.hatter.tools.commons.classloader;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.exception.ExceptionUtil;

public class ClassLoaderUtil {

    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static ClassLoader getClassLoaderByClass(Class<?> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        return (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
    }

    public static boolean isClassesCanBeLoad(String clazz0, String... clazzes) {
        List<String> allClasses = CollectionUtil.add(CollectionUtil.objectToList(clazz0), Arrays.asList(clazzes));
        for (String clazz : allClasses) {
            if (!isClassCanBeLoad(clazz)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isClassCanBeLoad(String clazz) {
        String name = clazz.replace('.', '/') + ".class";
        return (ClassLoaderUtil.class.getClassLoader().getResource(name) != null);
    }

    public static void defineClass(String name, byte[] b) {
        defineClass(ClassLoaderUtil.class.getClassLoader(), name, b);
    }

    public static void defineClass(ClassLoader classLoader, String name, byte[] b) {
        defineClass(classLoader, name, b, 0, b.length);
    }

    public static void defineClass(String name, byte[] b, int off, int len) {
        defineClass(ClassLoaderUtil.class.getClassLoader(), name, b, off, len);
    }

    public static void defineClass(ClassLoader classLoader, String name, byte[] b, int off, int len) {
        try {
            Method m = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class,
                    int.class, int.class });
            m.setAccessible(true);
            Object[] ags = new Object[] { name, b, off, len };
            m.invoke(classLoader, ags);
        } catch (Exception e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadClassAndNew(String clazz) {
        return newInstance((Class<T>) loadClass(clazz));
    }

    public static <T> Class<T> loadClass(String clazz) {
        return loadClass(ClassLoaderUtil.class.getClassLoader(), clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadClassAndNew(ClassLoader classLoader, String clazz) {
        return newInstance((Class<T>) loadClassAndNew(classLoader, clazz));
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(ClassLoader classLoader, String clazz) {
        try {
            return (Class<T>) classLoader.loadClass(clazz);
        } catch (ClassNotFoundException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }
}
