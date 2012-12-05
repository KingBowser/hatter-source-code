package me.hatter.tools.commons.misc;

import java.util.concurrent.Semaphore;

public class ShutdownSignal {

    // must be fair, otherwise may cannot shutdown
    private Semaphore semaphore = new Semaphore(1, true);

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }

    public ShutdownSignal() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    // when Ctrl+C, wait normal process finished
                    semaphore.acquire();
                    semaphore.release();
                } catch (InterruptedException e) {
                }
            }
        });
    }
}
