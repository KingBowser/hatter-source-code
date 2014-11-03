package me.hatter.tools.taskprocess.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DynamicSemaphore {

    private AtomicInteger permits          = new AtomicInteger(0);
    private AtomicInteger usedPermits      = new AtomicInteger(0);

    private ReentrantLock permitsLock      = new ReentrantLock();
    private Condition     permitsCondition = permitsLock.newCondition();

    public DynamicSemaphore(int permits) {
        setPermits(permits);
    }

    public int getPermits() {
        return permits.get();
    }

    public int getLeftPermits() {
        return (permits.get() - usedPermits.get());
    }

    public void setPermits(int permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException("Permits MUST greater than 0.");
        }
        this.permits.set(permits);
        trySignalCondition();
    }

    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    public boolean tryAcquire(int permits) {
        int usedPermits;
        do {
            usedPermits = this.usedPermits.get();
            if ((usedPermits + permits) > this.permits.get()) {
                return false;
            }
        } while (!this.usedPermits.compareAndSet(usedPermits, (usedPermits + permits)));
        return true;
    }

    public void acquire() throws InterruptedException {
        acquire(1);
    }

    public void acquire(int permits) throws InterruptedException {
        int usedPermits;
        do {
            usedPermits = this.usedPermits.get();
            while ((usedPermits + permits) > this.permits.get()) {
                permitsLock.lock();
                try {
                    permitsCondition.await();
                } finally {
                    permitsLock.unlock();
                }
                usedPermits = this.usedPermits.get();
            }
        } while (!this.usedPermits.compareAndSet(usedPermits, (usedPermits + permits)));

        trySignalCondition();
    }

    public void release() {
        release(1);
    }

    public void release(int permits) {
        this.usedPermits.addAndGet(-permits);
        trySignalCondition();
    }

    private void trySignalCondition() {
        if (this.usedPermits.get() < this.permits.get()) {
            permitsLock.lock();
            try {
                permitsCondition.signal();
            } finally {
                permitsLock.unlock();
            }
        }
    }

    public String toString() {
        return "DynamicSemaphore: " + getLeftPermits() + "/" + getPermits();
    }
}
