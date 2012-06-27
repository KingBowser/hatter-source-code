package me.hatter.tools.cook.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileUtil {

    public static void writeStringToFile(File file, String content) {
        try {
            IOUtil.writeStringAndClose(new FileOutputStream(file), content);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
