package me.hatter.tools.taskprocess.tests;

import me.hatter.tools.taskprocess.util.check.ProcessStopFlag;

public class ProcessStopCheckTest {

    public static void main(String[] a) {
        ProcessStopFlag check = new ProcessStopFlag();
        for (int i = 0; i < 11111111; i++) {
            try {
                Thread.sleep(111);
            } catch (InterruptedException e) {
            }
            System.out.println(check.checkStopFlag());
        }
    }
}
