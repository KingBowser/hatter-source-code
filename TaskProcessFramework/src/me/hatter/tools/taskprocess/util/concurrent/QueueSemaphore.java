package me.hatter.tools.taskprocess.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueSemaphore {

    private AtomicInteger          permits = new AtomicInteger(0);
    private BlockingQueue<Boolean> queue;

    public QueueSemaphore(int permits) {
        this(new LinkedBlockingQueue<Boolean>(), permits);
    }

    public QueueSemaphore(BlockingQueue<Boolean> queue, int permits) {
        if (queue == null) {
            throw new NullPointerException("Semaphore queue MUST NOT be null.");
        }
        this.queue = queue;
        setPermits(permits);
    }

    public int getPermits() {
        return permits.get();
    }

    public int getLeftPermits() {
        return queue.size();
    }

    public void acquire() throws InterruptedException {
        queue.take(); // take from queue, wait until the resource is available
    }

    public void acquire(int permits) throws InterruptedException {
        for (int i = 0; i < permits; i++) {
            acquire();
        }
    }

    synchronized public void acquireAll() throws InterruptedException {
        acquire(getPermits());
    }

    public void release() {
        // if reduce the queue permits, then do not put it back to the queue
        if (this.permits.get() > queue.size()) {
            queue.add(Boolean.TRUE);
        }
    }

    public void release(int permits) {
        for (int i = 0; i < permits; i++) {
            release();
        }
    }

    public String toString() {
        return "QueueSemaphore: " + getLeftPermits() + "/" + getPermits();
    }

    private void setPermits(int permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException("Semaphore count MUST greater than 0.");
        }
        // calculate the left lot, and add them
        int oldPermits = this.permits.get();
        int shouldAddPermits = permits - oldPermits;
        if (shouldAddPermits > 0) {
            for (int i = 0; i < shouldAddPermits; i++) {
                queue.add(Boolean.TRUE);
            }
        }
        this.permits.set(permits);
    }
}
