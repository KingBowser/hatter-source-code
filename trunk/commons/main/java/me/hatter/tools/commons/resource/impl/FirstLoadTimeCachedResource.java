package me.hatter.tools.commons.resource.impl;

import java.util.concurrent.TimeUnit;

import me.hatter.tools.commons.resource.Resource;

public class FirstLoadTimeCachedResource extends AbstractCachedResource {

    private long lastLoadMillis = 0L;
    private long cacheDeltaMillis;

    public FirstLoadTimeCachedResource(Resource targetResource) {
        super(targetResource);
        this.cacheDeltaMillis = TimeUnit.MINUTES.toMillis(10L);
    }

    public FirstLoadTimeCachedResource(Resource targetResource, long cacheDeltaMillis) {
        super(targetResource);
        this.cacheDeltaMillis = cacheDeltaMillis;
    }

    @Override
    protected boolean needUpdateCache() {
        if (lastLoadMillis == 0L) {
            return true;
        }
        return (System.currentTimeMillis() - lastLoadMillis) <= cacheDeltaMillis;
    }

    @Override
    protected void afterUpdateCache() {
        lastLoadMillis = System.currentTimeMillis();
    }
}
