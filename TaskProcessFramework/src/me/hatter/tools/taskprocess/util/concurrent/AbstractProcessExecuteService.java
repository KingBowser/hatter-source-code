package me.hatter.tools.taskprocess.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import me.hatter.tools.taskprocess.util.misc.ExceptionUtils;

public abstract class AbstractProcessExecuteService {

    private int           maxQueueCount;
    private AtomicInteger runningCount = new AtomicInteger(0);
    private AtomicInteger totalCount   = new AtomicInteger(0);
    private Semaphore     semaphore;

    public AbstractProcessExecuteService(int maxQueueCount) {
        this.maxQueueCount = maxQueueCount;
        System.out.println("[INFO] Process executor service; maxQueueCount: " + maxQueueCount);
    }

    public <T> Future<T> submit(final Callable<T> task) {
        totalCount.incrementAndGet();
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            // IGNORE
        }

        return getCurrentExecutor().submit(new Callable<T>() {

            public T call() throws Exception {
                runningCount.incrementAndGet();
                try {
                    return task.call();
                } catch (Exception e) {
                    System.out.println("[ERROR] Unknow exception occured thread:" + Thread.currentThread().getName()
                                       + " #" + Thread.currentThread().getId() + ", exception: "
                                       + ExceptionUtils.getStackTrace(e));
                    return null;
                } finally {
                    runningCount.decrementAndGet();
                    totalCount.decrementAndGet();
                    semaphore.release();
                }
            }
        });
    }

    public void waitUntilFinish() throws InterruptedException {
        semaphore.acquire(maxQueueCount); // all task finished
        semaphore.release(maxQueueCount);
    }

    abstract protected ExecutorService getCurrentExecutor();

    abstract protected List<ExecutorService> getAllExecutors();

    public void shutDown() {
        List<ExecutorService> executorServices = getAllExecutors();
        for (ExecutorService executorService : executorServices) {
            executorService.shutdown();
        }
    }

    public List<Runnable> shutdownNow() {
        List<Runnable> list = new ArrayList<Runnable>();
        List<ExecutorService> executorServices = getAllExecutors();
        for (ExecutorService executorService : executorServices) {
            list.addAll(executorService.shutdownNow());
        }
        return list;
    }

    public int getRunningCount() {
        return runningCount.get();
    }

    public int getTotalCount() {
        return totalCount.get();
    }
}
