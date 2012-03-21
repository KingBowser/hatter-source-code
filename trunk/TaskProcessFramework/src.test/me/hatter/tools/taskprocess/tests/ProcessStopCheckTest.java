package me.hatter.tools.taskprocess.tests;

import me.hatter.tools.taskprocess.util.check.ProcessStopCheck;

public class ProcessStopCheckTest {

    public static void main(String[] a) {
        ProcessStopCheck check = new ProcessStopCheck();
        for (int i = 0; i < 11111111; i++) {
            try {
                Thread.sleep(111);
            } catch (InterruptedException e) {
            }
            System.out.println(check.checkStopFlag());
        }
    }
}
