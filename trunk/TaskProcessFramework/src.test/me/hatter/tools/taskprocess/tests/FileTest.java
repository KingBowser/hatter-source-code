package me.hatter.tools.taskprocess.tests;

import java.io.File;
import java.io.IOException;

public class FileTest {

    public static void main(String[] a) {
        File f = new File("/Users/hatterjiang/xxxx");
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        f.setLastModified(System.currentTimeMillis() - 100000000);
    }
}
