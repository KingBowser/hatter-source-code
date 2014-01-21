package me.hatter.tools.commons.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import me.hatter.tools.commons.io.IOUtil;

public class DirWalkTool {

    public static interface DirWalker {

        boolean accept(File file);

        void readInputStream(InputStream is, File file);
    }

    private File dir;

    public DirWalkTool(File dir) {
        this.dir = dir;
    }

    public void walk(DirWalker walker) {
        walk(dir, walker);
    }

    protected void walk(File file, DirWalker walker) {
        if (file.isDirectory()) {
            if (walker.accept(file)) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        walk(f, walker);
                    }
                }
            }
        } else {
            if (walker.accept(file)) {
                try {
                    InputStream is = IOUtil.asBufferedInputStream(new FileInputStream(file));
                    try {
                        walker.readInputStream(is, file);
                    } finally {
                        IOUtil.closeQuietly(is);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e); // SHOULD NOT HAPPEN?
                }
            }
        }
    }
}
