package me.hatter.tools.taskprocess.util.concurrent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import me.hatter.tools.taskprocess.util.ProcessDelayProtection;

public class ProcessExecuteService {

    private int                maxQueueCount;
    private int                dayTimeThreadCount;
    private int                nightThreadCount;
    private AtomicInteger      runningCount = new AtomicInteger(0);
    private AtomicInteger      totalCount   = new AtomicInteger(0);
    private ThreadPoolExecutor dayTimeExecutor;
    private ThreadPoolExecutor nightExecutor;
    private Semaphore          semaphore;

    public ProcessExecuteService(int dayTimeThreadCount, int nightThreadCount) {
        this((Math.max(dayTimeThreadCount, nightThreadCount) + 10), dayTimeThreadCount, nightThreadCount);
    }

    public ProcessExecuteService(int maxQueueCount, int dayTimeThreadCount, int nightThreadCount) {
        this.maxQueueCount = maxQueueCount;
        this.dayTimeThreadCount = dayTimeThreadCount;
        this.nightThreadCount = nightThreadCount;
        // create excutors and semaphore
        dayTimeExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.dayTimeThreadCount);
        nightExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.nightThreadCount);
        semaphore = new Semaphore(this.maxQueueCount);
        System.out.println("[INFO] Process executor service; maxQueueCount: " + maxQueueCount
                           + ", dayTimeThreadCount: " + dayTimeThreadCount + ", nightThreadCount: " + nightThreadCount);
    }

    public <T> Future<T> submit(final Callable<T> task) {
        totalCount.incrementAndGet();
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            // IGNORE
        }

        return getExecutor().submit(new Callable<T>() {

            public T call() throws Exception {
                runningCount.incrementAndGet();
                try {
                    return new ProcessDelayProtection<T>(task).call();
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

    public void shutDown() {
        dayTimeExecutor.shutdown();
        nightExecutor.shutdown();
    }

    public List<Runnable> shutdownNow() {
        List<Runnable> list = new ArrayList<Runnable>();
        list.addAll(dayTimeExecutor.shutdownNow());
        list.addAll(nightExecutor.shutdownNow());
        return list;
    }

    public int getRunningCount() {
        return runningCount.get();
    }

    public int getTotalCount() {
        return totalCount.get();
    }

    protected ExecutorService getExecutor() {
        return (isDayTime()) ? dayTimeExecutor : nightExecutor;
    }

    protected boolean isDayTime() {
        int hzHour = getHzHour();
        return ((hzHour > 8) && (hzHour < 20));
    }

    protected Integer getHzHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return new Integer(sdf.format(new Date()));
    }
}
