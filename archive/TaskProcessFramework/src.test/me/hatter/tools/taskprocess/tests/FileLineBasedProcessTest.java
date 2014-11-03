package me.hatter.tools.taskprocess.tests;

import me.hatter.tools.taskprocess.util.FileLineBasedProcess;

public class FileLineBasedProcessTest extends FileLineBasedProcess {

    public static void main(String[] a) {
        (new FileLineBasedProcessTest()).mainProcss();
    }

    protected void doProcess(String line, boolean isDryRun) throws Exception {
        Thread.sleep(100);
        dataLog.println("XX: " + line);
        System.out.println(thisCount.get() + " --> " + line);
    }
}
