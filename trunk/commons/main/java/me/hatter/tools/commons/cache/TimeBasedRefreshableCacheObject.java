package me.hatter.tools.commons.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import me.hatter.tools.commons.assertion.AssertUtil;

public abstract class TimeBasedRefreshableCacheObject<T> extends RefreshableCacheObject<T> {

    private long lastRefreshMillis    = 0L;
    private long updateIntervalMillis = TimeUnit.MINUTES.toMillis(5);

    public TimeBasedRefreshableCacheObject() {
    }

    public TimeBasedRefreshableCacheObject(long updateIntervalMillis) {
        AssertUtil.isTrue(updateIntervalMillis > 0);
        this.updateIntervalMillis = updateIntervalMillis;
    }

    public TimeBasedRefreshableCacheObject(long updateInterval, TimeUnit unit) {
        AssertUtil.isTrue(updateInterval > 0);
        AssertUtil.isNotNull(unit);
        this.updateIntervalMillis = unit.toMillis(updateInterval);
    }

    @Override
    protected boolean shouldRefresh() {
        return (Math.abs(System.currentTimeMillis() - lastRefreshMillis) >= updateIntervalMillis);
    }

    @Override
    protected void updateRefreshObject(Callable<T> refreshAction) throws Exception {
        super.updateRefreshObject(refreshAction);
        this.lastRefreshMillis = System.currentTimeMillis();
    }
}
