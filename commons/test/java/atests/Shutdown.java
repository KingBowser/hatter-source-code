package atests;

import me.hatter.tools.commons.misc.ShutdownSignal;

public class Shutdown {

    static ShutdownSignal s = new ShutdownSignal();

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            s.acquire();
            try {
                System.out.println("aaaaaaaaa");
                Thread.sleep(1000);
                System.out.println("bbbbbbbbb");
                Thread.sleep(1000);
                System.out.println("ccccccccc");
                Thread.sleep(1000);
                System.out.println("ddddddddd");
                Thread.sleep(1000);
            } finally {
                s.release();
            }
        }
    }
}
