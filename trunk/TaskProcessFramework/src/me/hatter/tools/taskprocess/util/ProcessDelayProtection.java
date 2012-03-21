package me.hatter.tools.taskprocess.util;

import java.util.concurrent.Callable;

import me.hatter.tools.taskprocess.util.misc.ExceptionUtils;

public class ProcessDelayProtection<T> implements Callable<T> {

    private static boolean    protectionOn = Boolean.valueOf(System.getProperty("protectionon", "true"));
    static {
        System.out.println("[INFO] Protection mode: " + (protectionOn ? "ON" : "OFF"));
    }
    private static final long MAX_SLEEP    = 10000;
    private int               minCost;
    private int               maxCost;
    private Callable<T>       callable;

    public ProcessDelayProtection(Callable<T> callable) {
        this(callable, 100, 200);
    }

    public ProcessDelayProtection(Callable<T> callable, int minCost, int maxCost) {
        this.callable = callable;
        this.minCost = minCost;
        this.maxCost = maxCost;
        System.out.println("[INFO] Process delay protection; min: " + minCost + ", max: " + maxCost);
    }

    public T call() throws Exception {
        long start = System.currentTimeMillis();
        long end;
        T result = null;
        try {
            result = callable.call();
            end = System.currentTimeMillis();
        } catch (Exception ex) {
            System.out.println("[ERROR] Exception occured: " + ExceptionUtils.getStackTrace(ex));
            end = start + maxCost;
        }
        long cost = end - start;
        long delay = getDelayMillis(cost);
        if (delay > 0) {
            if (delay > 10) {
                System.out.println("[INFO] ProcessDelayProtection: Sleep: " + delay);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
        return result;
    }

    long getDelayMillis(long cost) {
        if (protectionOn) {
            if (cost < minCost) {
                return 0;
            }
            if (cost > maxCost) {
                return MAX_SLEEP;
            }
            return MAX_SLEEP / (maxCost - minCost) * (cost - minCost);
        } else {
            return 0;
        }
    }
}
