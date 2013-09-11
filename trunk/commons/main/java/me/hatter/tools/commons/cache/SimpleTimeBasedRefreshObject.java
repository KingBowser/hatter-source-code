package me.hatter.tools.commons.cache;

import java.util.concurrent.TimeUnit;

public abstract class SimpleTimeBasedRefreshObject<T> {

    private long          updateIntervalMillis = TimeUnit.MINUTES.toMillis(10);
    private volatile long lastUpdateMillis     = 0;
    private volatile T    obj                  = null;

    public SimpleTimeBasedRefreshObject(long updateIntervalMillis) {
        this.updateIntervalMillis = updateIntervalMillis;
    }

    public SimpleTimeBasedRefreshObject(long updateInterval, TimeUnit timeUnit) {
        this.updateIntervalMillis = timeUnit.toMillis(updateInterval);
    }

    public T getObject() {
        updateObject();
        return obj;
    }

    synchronized protected void updateObject() {
        if (obj == null) {
            obj = refreshObject();
            lastUpdateMillis = System.currentTimeMillis();
        } else {
            if ((System.currentTimeMillis() - lastUpdateMillis) > updateIntervalMillis) {
                obj = refreshObject();
                lastUpdateMillis = System.currentTimeMillis();
            }
        }
    }

    abstract protected T refreshObject();
}
