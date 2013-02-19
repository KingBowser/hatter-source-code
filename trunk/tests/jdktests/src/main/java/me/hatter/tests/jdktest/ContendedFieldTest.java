package me.hatter.tests.jdktest;

public class ContendedFieldTest {

    // @sun.misc.Contended
    public static class ContendedClass {

        private volatile long a;
        private volatile long b;
    }

    public static void main(String[] args) {

        for (int x = 0; x < 20; x++) {
            final long COUNT = 100000000L;
            final ContendedClass cc = new ContendedClass();
            Thread ta = new Thread() {

                public void run() {
                    for (long i = 0; i < COUNT; i++) {
                        cc.a += i;
                    }
                }
            };
            Thread tb = new Thread() {

                public void run() {
                    for (long i = 0; i < COUNT; i++) {
                        cc.b += i;
                    }
                }
            };
            long start = System.currentTimeMillis();
            ta.start();
            tb.start();
            try {
                ta.join();
                tb.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            System.out.println("Cost: " + (end - start) + " ms" + " @" + x);
        }
    }
}
