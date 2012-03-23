package me.hatter.tools.taskprocess.tests;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.hatter.tools.taskprocess.util.concurrent.DynamicSemaphore;

public class SemaphoreTest {

    public static void main(String[] a) {

        final DynamicSemaphore semaphore = new DynamicSemaphore(20);
        ExecutorService es = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 80; i++) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final int x = i;
            if ((i + 1) % 31 == 0) {
                semaphore.setPermits(3);
            }
            es.submit(new Callable<Void>() {

                public Void call() throws Exception {
                    try {
                        Thread.sleep(1000);
                        System.out.println("C " + Thread.currentThread().getName() + " " + x + " " + semaphore);
                    } finally {
                        semaphore.release();
                    }
                    return null;
                }
            });
        }
        System.out.println("wait all!!!!!!!! " + semaphore);
        try {
            semaphore.acquireAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finish !!!!!!!!");
        es.shutdown();
    }
}
