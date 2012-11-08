package me.hatter.jprofiler.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static String readFile(File file) {
        return readFile(file, DEFAULT_CHARSET);
    }

    public static String readFile(File file, String charset) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            StringBuilder sb = new StringBuilder();
            for (int c; ((c = br.read()) != -1);) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuality(br);
        }
    }

    public static void closeQuality(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // IGNORE
            }
        }
    }

    public static List<File> listFiles(String dir, final String prefix, final String pid) {
        File file = new File(dir);
        File[] files = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(prefix + "_" + pid + ".");
            }
        });
        return Arrays.asList(files);
    }
}
