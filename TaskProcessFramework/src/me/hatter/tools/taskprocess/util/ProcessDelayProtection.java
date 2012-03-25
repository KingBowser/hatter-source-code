package me.hatter.tools.taskprocess.util;

import java.util.concurrent.Callable;

import me.hatter.tools.taskprocess.util.env.Env;
import me.hatter.tools.taskprocess.util.misc.ExceptionUtils;

public class ProcessDelayProtection<T> implements Callable<T> {

    private static boolean    protectionOn = Env.getBoolProperty("protectionon", true);
    private static final long MAX_SLEEP    = Env.getLongProperty("maxsleep", 10000);
    private static final long LOG_MILLS    = Env.getLongProperty("logmills", 10);
    private static final int  DEF_MIN_COST = Env.getIntProperty("defmincost", 400);
    private static final int  DEF_MAX_COST = Env.getIntProperty("defmaxcost", 600);
    private int               minCost;
    private int               maxCost;
    private Callable<T>       callable;

    public ProcessDelayProtection(Callable<T> callable) {
        this(callable, DEF_MIN_COST, DEF_MAX_COST);
    }

    public ProcessDelayProtection(Callable<T> callable, int minCost, int maxCost) {
        this.callable = callable;
        this.minCost = minCost;
        this.maxCost = maxCost;
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
            if (delay > LOG_MILLS) {
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
