package me.hatter.tools.taskprocess.tests;

import java.util.Arrays;
import java.util.concurrent.Callable;

import me.hatter.tools.taskprocess.util.concurrent.DayNightProcessExecuteService;

public class ProcessExecuteServiceTest {

    public static void main(String[] a) {
        final DayNightProcessExecuteService pes = new DayNightProcessExecuteService(2, 3, 10);
        for (int time : Arrays.asList(10, 20, 10, 20, 30, 40, 50, 100, 20, 40, 50, 50, 100, 200)) {
            final int ftime = time;
            pes.submit(new Callable<Void>() {

                public Void call() throws Exception {
                    Thread.sleep(ftime);
                    System.out.println("[INFO] RUNING: " + pes.getRunningCount());
                    return null;
                }
            });
        }
        try {
            pes.waitUntilFinish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[INFO] Finish all!");
        pes.shutDown();
    }
}
