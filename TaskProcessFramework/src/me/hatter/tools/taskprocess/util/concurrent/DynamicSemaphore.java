package me.hatter.tools.taskprocess.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DynamicSemaphore {

    private AtomicInteger permits          = new AtomicInteger(0);
    private AtomicInteger leftPermits      = new AtomicInteger(0);

    private ReentrantLock permitsLock      = new ReentrantLock();
    private Condition     permitsCondition = permitsLock.newCondition();

    public DynamicSemaphore(int permits) {
        setPermits(permits);
    }

    public int getPermits() {
        return permits.get();
    }

    public int getLeftPermits() {
        return leftPermits.get();
    }

    public void setPermits(int permits) {
        permitsLock.lock();
        try {
            int oldPermits = this.permits.get();
            this.permits.set(permits);
            this.leftPermits.addAndGet(permits - oldPermits);
        } finally {
            permitsLock.unlock();
        }
    }

    public void acquire() throws InterruptedException {
        acquire(1);
    }

    public void acquire(int permits) throws InterruptedException {
        permitsLock.lock();
        try {
            int leftPermits;
            do {
                leftPermits = this.leftPermits.get();
                if ((leftPermits - permits) < 0) {
                    permitsCondition.await();
                }
            } while (!this.leftPermits.compareAndSet(leftPermits, (leftPermits - permits)));
            if (this.leftPermits.get() > 0) {
                permitsCondition.signal();
            }
        } finally {
            permitsLock.unlock();
        }
    }

    public void acquireAll() throws InterruptedException {
        permitsLock.lock();
        try {
            acquire(getPermits());
        } finally {
            permitsLock.unlock();
        }
    }

    public void release() {
        release(1);
    }

    public void release(int permits) {
        permitsLock.lock();
        try {
            this.leftPermits.addAndGet(permits);
            permitsCondition.signal();
        } finally {
            permitsLock.unlock();
        }
    }

    public String toString() {
        return "DynamicSemaphore: " + getLeftPermits() + "/" + getPermits();
    }
}
