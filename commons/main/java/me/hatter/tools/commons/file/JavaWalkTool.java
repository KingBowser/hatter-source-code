package me.hatter.tools.commons.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.hatter.tools.commons.io.IOUtil;

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

    abstract public static class AbstractClassReaderJarWalker<T> extends AbstractClassJarJavaWalker {

        private boolean    isVerbose;
        private boolean    isTrace;
        private AtomicLong processedCount = new AtomicLong(0);

        public AbstractClassReaderJarWalker(boolean isVerbose, boolean isTrace) {
            this.isVerbose = isVerbose;
            this.isTrace = isTrace;
        }

        public void readInputStream(InputStream is, File file, String name, AcceptType type) {
            processedCount.incrementAndGet();
            if (isVerbose) {
                if (type == AcceptType.File) {
                    System.out.println("Read file: " + file.getPath());
                }
                if (type == AcceptType.Entry) {
                    System.out.println("Read entry: " + file.getPath() + "!" + name);
                }
            } else if (isTrace && ((processedCount.get() % 100) == 0)) {
                System.out.print(".");
            }
            byte[] bytes = IOUtil.readToBytesAndClose(new BufferedInputStream(is));
            try {
                dealClass(((type == AcceptType.Entry) ? file : null), readClass(bytes), bytes);
            } catch (Exception e) {
                handleException(is, file, name, type, e);
            }
        }

        protected void handleException(InputStream is, File file, String name, AcceptType type, Exception e) {
            System.out.println("[ERROR] read class: " + file + "!" + name + " failed: " + e.getMessage());
        }

        abstract protected T readClass(byte[] bytes);

        abstract protected void dealClass(File jarFile, T t, byte[] bytes);
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
                    try {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry = entries.nextElement();
                            if (walker.accept(null, jarEntry.getName(), AcceptType.Entry)) {
                                InputStream is = jarFile.getInputStream(jarEntry);
                                try {
                                    walker.readInputStream(is, file, jarEntry.getName(), AcceptType.Entry);
                                } finally {
                                    IOUtil.closeQuietly(is);
                                }
                            }
                        }
                    } finally {
                        jarFile.close();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (walker.accept(file, file.getName(), AcceptType.File)) {
                try {
                    InputStream is = new BufferedInputStream(new FileInputStream(file));
                    try {
                        walker.readInputStream(is, file, file.getName(), AcceptType.File);
                    } finally {
                        IOUtil.closeQuietly(is);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
