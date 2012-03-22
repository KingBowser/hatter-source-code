package me.hatter.tools.taskprocess.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import me.hatter.tools.taskprocess.util.concurrent.exception.DefaultExceptionHandler;
import me.hatter.tools.taskprocess.util.concurrent.exception.ExceptionHandler;
import me.hatter.tools.taskprocess.util.misc.ExceptionUtils;

public abstract class AbstractProcessExecuteService {

    private int           maxQueueCount;
    private AtomicInteger runningCount = new AtomicInteger(0);
    private AtomicInteger totalCount   = new AtomicInteger(0);
    private Semaphore     semaphore;

    public AbstractProcessExecuteService(int maxQueueCount) {
        this.maxQueueCount = maxQueueCount;
        this.semaphore = new Semaphore(this.maxQueueCount);
        System.out.println("[INFO] Process executor service; maxQueueCount: " + maxQueueCount);
    }

    abstract protected ExecutorService getCurrentExecutor();

    abstract protected List<ExecutorService> getAllExecutors();

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
                } catch (Throwable t) {
                    if (t instanceof Exception) {
                        try {
                            getExceptionHandle().handle((Exception) t);
                        } catch (Exception e) {
                            // GOD, something serious occured!
                            System.out.println("[ERROR] Unknow Exception in handle exception, thread:"
                                               + Thread.currentThread().getName() + " #"
                                               + Thread.currentThread().getId() + ", exception: "
                                               + ExceptionUtils.getStackTrace(t));
                        }
                    } else {
                        // GOD, something serious occured!
                        System.out.println("[ERROR] GOD, Unknow ERROR occured thread:"
                                           + Thread.currentThread().getName() + " #" + Thread.currentThread().getId()
                                           + ", exception: " + ExceptionUtils.getStackTrace(t));
                    }
                    return null;
                } finally {
                    runningCount.decrementAndGet();
                    totalCount.decrementAndGet();
                    semaphore.release();
                }
            }
        });
    }

    public final void waitUntilFinish() throws InterruptedException {
        semaphore.acquire(maxQueueCount); // all task finished
        semaphore.release(maxQueueCount);
    }

    public final int getRunningCount() {
        return runningCount.get();
    }

    public final int getTotalCount() {
        return totalCount.get();
    }

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

    protected ExceptionHandler getExceptionHandle() {
        return new DefaultExceptionHandler();
    }
}
