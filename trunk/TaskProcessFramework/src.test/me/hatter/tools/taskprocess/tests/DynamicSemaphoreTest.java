package me.hatter.tools.taskprocess.tests;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.hatter.tools.taskprocess.util.concurrent.DynamicSemaphore;

public class DynamicSemaphoreTest {

    public static void main(String[] a) throws InterruptedException {
        final DynamicSemaphore dy = new DynamicSemaphore(10);

        long s = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 1000000; i++) {
            es.submit(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    dy.acquire(3);
                    dy.release();
                    dy.release();
                    dy.release();

                    dy.acquire();
                    dy.acquire();
                    dy.acquire();
                    dy.release(3);
                    return null;
                }
            });
        }
        es.shutdown();
        long e = System.currentTimeMillis();
        System.out.println("EEEE " + (e - s));
    }
}
