package me.hatter.tools.taskprocess.tests;

import java.io.File;

import me.hatter.tools.taskprocess.util.env.Env;
import me.hatter.tools.taskprocess.util.io.FilePrintWriter;


public class WriteTestFile {

    public static void main(String[] a) throws Exception {
        File f = new File(Env.USER_DIR, "data.txt");
        FilePrintWriter writer = new FilePrintWriter(f);
        for (int i = 0; i < 10000; i++) {
            writer.println(i);
        }
        writer.close();
    }
}
