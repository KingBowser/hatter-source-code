package me.hatter.tools.taskprocess.tests;

import me.hatter.tools.taskprocess.util.FileLineBasedProcess;

public class FileLineBasedProcessTest extends FileLineBasedProcess {

    public static void main(String[] a) {
        (new FileLineBasedProcessTest()).mainProcss();
    }

    protected void doProcess(String line) throws Exception {
        Thread.sleep(100);
        dataLog.println(line);
        System.out.println(thisCount.get() + " --> " + line);
    }
}
