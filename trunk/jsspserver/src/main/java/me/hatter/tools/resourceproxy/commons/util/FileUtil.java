package me.hatter.tools.resourceproxy.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class FileUtil {

    public static final String SEPARATER         = File.separator;
    public static final String DOUBLE_SEPARATER  = SEPARATER + SEPARATER;
    public static final String ANOTHER_SEPARATER = "/".equals(SEPARATER) ? "\\" : "/";

    public static void writeStringToFile(File file, String str) {
        writeStringToFile(file, str, null);
    }

    public static void writeStringToFile(File file, String str, String charset) {
        try {
            OutputStreamWriter osw;
            if (charset == null) {
                osw = new OutputStreamWriter(new FileOutputStream(file));
            } else {
                osw = new OutputStreamWriter(new FileOutputStream(file), charset);
            }
            osw.write(str);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readFileToBytes(File file) {
        InputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtil.copy(fis, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.closeQuitely(fis);
        }
    }

    public static final String readFileToString(File file) {
        return readFileToString(file, null);
    }

    public static final String readFileToString(File file, String charset) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            BufferedReader br = getFileBufferedReader(file, charset);
            try {
                for (String line; (line = br.readLine()) != null;) {
                    pw.println(line);
                }
            } finally {
                IOUtil.closeQuitely(br);
            }

            pw.flush();
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedReader getFileBufferedReader(File file, String charset) {
        try {
            InputStreamReader isr;
            if (charset == null) {
                isr = new FileReader(file);
            } else {
                isr = new InputStreamReader(new FileInputStream(file), charset);
            }
            return new BufferedReader(isr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeFilesEndsWith(File path, String suffix) {
        if (path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(suffix)) {
                    file.delete();
                }
            }
        }
    }

    public static String combilePath(String path1, String path2) {
        return formatPath(path1 + SEPARATER + path2);
    }

    public static String formatPath(String path) {
        path = path.replace(ANOTHER_SEPARATER, SEPARATER);

        while (path.indexOf(DOUBLE_SEPARATER) > -1) {
            path = path.replace(DOUBLE_SEPARATER, SEPARATER);
        }
        return path;
    }
}
