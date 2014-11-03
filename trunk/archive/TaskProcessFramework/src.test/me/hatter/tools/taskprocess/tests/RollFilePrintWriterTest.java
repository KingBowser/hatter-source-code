package me.hatter.tools.taskprocess.tests;

import me.hatter.tools.taskprocess.util.io.RollFilePrintWriter;

public class RollFilePrintWriterTest {

    public static void main(String[] a) throws Exception {
        RollFilePrintWriter writer = new RollFilePrintWriter("/Users/hatterjiang/Desktop/", "x.txt", 160);
        for (int i = 0; i < 100; i++) {
            writer.println("" + i);
        }
        writer.close();
    }
}
