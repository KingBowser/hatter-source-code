package me.hatter.tools.taskprocess.tests;

import me.hatter.tools.taskprocess.util.concurrent.DynamicSemaphore;

public class DynamicSemaphoreTest {

    public static void main(String[] a) throws InterruptedException {
        DynamicSemaphore dy = new DynamicSemaphore(10);

        long s = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            dy.acquire(3);
            dy.release();
            dy.release();
            dy.release();

            dy.acquire();
            dy.acquire();
            dy.acquire();
            dy.release(3);
        }
        long e = System.currentTimeMillis();
        System.out.println("EEEE " + (e - s));
    }
}
