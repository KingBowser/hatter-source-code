package me.hatter.tools.thread;

public class ThreadTest {

    public static void main(String[] args) {
        final Runnable r1 = new Runnable() {

            public void run() {
                System.out.println("R1:::");
                try {
                    Thread.sleep(1000000000000L);
                } catch (InterruptedException e) {
                }
            }
        };
        final Runnable r2 = new Runnable() {

            public void run() {
                System.out.println("R2:::");
                Thread t1 = new Thread(r1);
                t1.setName("T1");
                t1.start();
                try {
                    Thread.sleep(1000000000000L);
                } catch (InterruptedException e) {
                }
            }
        };

        Thread t2 = new Thread(r2);
        t2.setName("T2");
        t2.start();
    }
}
