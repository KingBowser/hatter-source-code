package me.hatter.tools.commons.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JavaWalkTool {

    public static enum AcceptType {
        File,

        Entry,

        Directory,

        JarFile
    }

    public static interface JavaWalker {

        boolean isJar(File file, String name);

        boolean accept(File file, String name, AcceptType type);

        void readInputStream(InputStream is, File file, String name, AcceptType type);
    }

    abstract public static class AbstractClassJarJavaWalker implements JavaWalker {

        public boolean isJar(File file, String name) {
            return name.endsWith(".jar");
        }

        public boolean accept(File file, String name, AcceptType type) {
            if (type == AcceptType.Directory) {
                return (!file.getAbsolutePath().contains(".svn"));
            }
            if (type == AcceptType.JarFile) {
                return true;
            }
            if (type == AcceptType.Entry) {
                return name.endsWith(".class");
            }
            return name.endsWith(".class");
        }
    }

    private File dir;

    public JavaWalkTool(File dir) {
        this.dir = dir;
    }

    public void walk(JavaWalker walker) {
        walk(dir, walker);
    }

    protected void walk(File file, JavaWalker walker) {
        if (file.isDirectory()) {
            if (walker.accept(file, file.getName(), AcceptType.Directory)) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        walk(f, walker);
                    }
                }
            }
        } else {
            if (walker.isJar(file, file.getName()) && walker.accept(file, file.getName(), AcceptType.JarFile)) {
                try {
                    JarFile jarFile = new JarFile(file);
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        if (walker.accept(null, jarEntry.getName(), AcceptType.Entry)) {
                            InputStream is = jarFile.getInputStream(jarEntry);
                            walker.readInputStream(is, file, jarEntry.getName(), AcceptType.Entry);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (walker.accept(file, file.getName(), AcceptType.File)) {
                try {
                    walker.readInputStream(new BufferedInputStream(new FileInputStream(file)), file, file.getName(),
                                           AcceptType.File);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
