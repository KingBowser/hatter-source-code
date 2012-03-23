package me.hatter.tools.taskprocess.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class OriDynamicSemaphore {

    private AtomicInteger permits     = new AtomicInteger(0);
    private AtomicInteger leftPermits = new AtomicInteger(0);
    private Object        lock        = new Object();

    public OriDynamicSemaphore(int permits) {
        setPermits(permits);
    }

    public int getPermits() {
        return permits.get();
    }

    public int getLeftPermits() {
        return leftPermits.get();
    }

    public void setPermits(int permits) {
        synchronized (lock) {
            int oldPermits = this.permits.get();
            this.permits.set(permits);
            this.leftPermits.addAndGet(permits - oldPermits);
        }
    }

    public void acquire() throws InterruptedException {
        acquire(1);
    }

    public void acquire(int permits) throws InterruptedException {
        synchronized (lock) {
            int leftPermits;
            do {
                leftPermits = this.leftPermits.get();
                if ((leftPermits - permits) < 0) {
                    lock.wait();
                }
            } while (!this.leftPermits.compareAndSet(leftPermits, (leftPermits - permits)));
            if (this.leftPermits.get() > 0) {
                lock.notify();
            }
        }
    }

    public void acquireAll() throws InterruptedException {
        synchronized (lock) {
            acquire(getPermits());
        }
    }

    public void release() {
        release(1);
    }

    public void release(int permits) {
        synchronized (lock) {
            this.leftPermits.addAndGet(permits);
            if (this.leftPermits.get() > 0) {
                lock.notify();
            }
        }
    }

    public String toString() {
        return "DynamicSemaphore: " + getLeftPermits() + "/" + getPermits();
    }
}
