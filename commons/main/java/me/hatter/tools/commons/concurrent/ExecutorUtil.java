package me.hatter.tools.commons.concurrent;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

public class ExecutorUtil {

    public static void sleep(long time, TimeUnit unit) {
        sleep(unit.toMillis(time));
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // IGNORE
        }
    }

    public static ExecutorService getCPULikeExecutor(Integer count) {
        int processorCount = (count != null) ? count.intValue() : ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        ExecutorService executor;
        if (processorCount <= 1) {
            executor = new OneThreadExecutor();
        } else {
            executor = new ThreadPoolExecutor(processorCount - 1, processorCount - 1, 0L, TimeUnit.MILLISECONDS,
                                              new LinkedBlockingQueue<Runnable>(processorCount),
                                              Executors.defaultThreadFactory(), new CallerRunsPolicy());
        }
        return executor;
    }
}
