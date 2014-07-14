package me.hatter.tools.commons.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import me.hatter.tools.commons.io.IOUtil;

public class FileUtil {

    @Deprecated
    // Environment.LINE_SEPARATOR
    public static final String SEPARATER = File.separator;

    public static void writeStringToFile(File file, String content) {
        writeStringToFile(file, content, IOUtil.CHARSET_UTF8);
    }

    public static void writeStringToFile(File file, String content, String charset) {
        try {
            IOUtil.writeStringAndClose(new FileOutputStream(file), content, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFileToStringIfExists(File file) {
        if ((file == null) || (!file.exists())) {
            return null;
        }
        return readFileToString(file);
    }

    public static String readFileToString(File file) {
        return readFileToString(file, IOUtil.CHARSET_UTF8);
    }

    public static String readFileToStringIfExists(File file, String charset) {
        if ((file == null) || (!file.exists())) {
            return null;
        }
        return readFileToString(file, charset);
    }

    public static String readFileToString(File file, String charset) {
        try {
            return IOUtil.readToStringAndClose(new BufferedInputStream(new FileInputStream(file)), charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeBytesToFile(File file, byte[] bytes) {
        try {
            IOUtil.writeBytesAndClose(new BufferedOutputStream(new FileOutputStream(file)), bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readFileToBytes(File file) {
        try {
            return IOUtil.readToBytesAndClose(new FileInputStream(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void listFiles(File dir, FileFilter fileFilter, List<File> refFiles) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (fileFilter.accept(f)) {
                    if (refFiles != null) {
                        refFiles.add(f);
                    }
                    if (f.isDirectory()) {
                        listFiles(f, fileFilter, refFiles);
                    }
                }
            }
        }
    }
}
