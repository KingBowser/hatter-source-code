package me.hatter.tools.commons.file;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JavaUtil {

    public static interface JavaWalker {

        boolean accept(File dir);

        boolean accept(JarEntry jarEntry);

        void read(File file);

        void read(InputStream is, String name);
    }

    public static void walk(File file, boolean extraceJars, JavaWalker walker) {
        if (file.isDirectory()) {
            if (walker.accept(file)) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        walk(f, extraceJars, walker);
                    }
                }
            }
        } else {
            if (extraceJars && file.toString().endsWith(".jar")) {
                try {
                    JarFile jarFile = new JarFile(file);
                    try {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry = entries.nextElement();
                            if (walker.accept(jarEntry)) {
                                InputStream is = jarFile.getInputStream(jarEntry);
                                walker.read(is, jarEntry.getName());
                            }
                        }
                    } finally {
                        jarFile.close();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                walker.read(file);
            }
        }
    }
}
