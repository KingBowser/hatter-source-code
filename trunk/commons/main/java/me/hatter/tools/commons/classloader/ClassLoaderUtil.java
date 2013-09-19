package me.hatter.tools.commons.classloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;

public class ClassLoaderUtil {

    private static final LogTool logTool = LogTools.getLogTool(ClassLoaderUtil.class);

    public static URLClassLoader getSystemClassLoader() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (cl == null) {
            throw new Error("System classloader is null.");
        }
        if (cl instanceof URLClassLoader) {
            return (URLClassLoader) cl;
        }
        throw new RuntimeException("System classloader is not extends from URLClassLoader: " + cl.getClass());
    }

    public static void addURLs(URLClassLoader ucl, URL... urls) {
        if ((urls == null) || (urls.length == 0)) {
            return;
        }
        try {
            Method m = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            m.setAccessible(true);
            for (URL url : urls) {
                m.invoke(ucl, new Object[] { url });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("restriction")
    public static URLClassLoader getAppClassLoader() {
        try {
            Field loader = sun.misc.Launcher.class.getDeclaredField("loader");
            loader.setAccessible(true);
            return (URLClassLoader) loader.get(sun.misc.Launcher.getLauncher());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static URLClassLoader getExtClassLoader() {
        return (URLClassLoader) getAppClassLoader().getParent();
    }

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

    public static String findClassJarPath(Object classOrName) {
        String className;
        if (classOrName instanceof String) {
            className = (String) classOrName;
        } else if (classOrName instanceof Class) {
            className = ((Class<?>) classOrName).getName();
        } else {
            throw new RuntimeException("Argument classOrName must be String or Class.");
        }
        URL u = getClassLoaderByClass(ClassLoaderUtil.class).getResource(className.replace('.', '/') + ".class");
        if (u != null) {
            String f = u.getFile();
            f = f.substring(0, f.indexOf(".jar!/") + 4);
            if (f.toLowerCase().startsWith("file:")) {
                f = f.substring("file:".length());
            }
            return f;
        }
        return null;
    }

    public static void addResourceToSystemClassLoader(String resource) {
        addResourceToSystemClassLoader(resource, true);
    }

    public static void addResourceToSystemClassLoader(String resource, boolean isPrintLog) {
        try {
            String r = resource.startsWith("/") ? resource : ("/" + resource);
            File tempjline = File.createTempFile("temp-jar", ".jar");
            tempjline.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempjline);
            IOUtil.copy(ClassLoaderUtil.class.getResourceAsStream(r), fos);
            fos.close();
            if (isPrintLog) {
                logTool.info("Generate jar and add to system class loader: " + tempjline + " (from " + r + ")");
            }
            ClassLoaderUtil.addURLs(ClassLoaderUtil.getSystemClassLoader(), tempjline.toURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void initLibResources() {
        initLibResources(true);
    }

    public static void initLibResources(boolean abortOnError) {
        try {
            String libresourcesJars = IOUtil.readResourceToString(ClassLoaderUtil.class, "/libresources_jars.txt");
            BufferedReader br = new BufferedReader(new StringReader(libresourcesJars));
            try {
                for (String line; ((line = br.readLine()) != null);) {
                    if (!line.trim().isEmpty()) {
                        addResourceToSystemClassLoader("/libresources/" + line.trim(),
                                                       Boolean.getBoolean("printInitLibResourcesLog"));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException ex) {
            if (abortOnError) {
                throw ex;
            } else {
                logTool.warn("Ignore init lib resources error: " + ex.getMessage());
            }
        }
    }
}
