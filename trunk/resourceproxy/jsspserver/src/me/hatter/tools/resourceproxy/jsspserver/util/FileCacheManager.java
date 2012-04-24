package me.hatter.tools.resourceproxy.jsspserver.util;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import me.hatter.tools.resourceproxy.commons.resource.Resource;
import me.hatter.tools.resourceproxy.commons.resource.Resources;

public class FileCacheManager {

    public static class TimeControlBytes {

        private long   cacheMills;
        private byte[] bytes;

        public TimeControlBytes(byte[] bytes) {
            this.cacheMills = System.currentTimeMillis();
            this.bytes = bytes;
        }
    }

    private static final long                      CACHE_BYTES    = 300 * 1024;
    private static final long                      CACHE_SIZE     = 1000;
    private static final long                      CACHE_MILLS    = TimeUnit.MINUTES.toMillis(5);
    private static BlockingQueue<Resource>         fileCacheQueue = new LinkedBlockingQueue<Resource>();
    private static Map<Resource, TimeControlBytes> fileCacheMap   = new WeakHashMap<Resource, TimeControlBytes>();

    public static byte[] readCacheFile(Resource resource, AtomicBoolean isFromCache) {
        if (isFromCache != null) {
            isFromCache.set(false);
        }
        TimeControlBytes timeControlBytes = null;
        synchronized (fileCacheQueue) {
            timeControlBytes = fileCacheMap.get(resource);
            if (timeControlBytes != null) {
                if ((timeControlBytes.cacheMills + CACHE_MILLS) < System.currentTimeMillis()) {
                    // timeout
                    fileCacheQueue.remove(resource);
                    fileCacheMap.remove(resource);
                    timeControlBytes = null;
                }
            }
        }
        if (timeControlBytes != null) {
            if (isFromCache != null) {
                isFromCache.set(true);
            }
            return timeControlBytes.bytes;
        }
        byte[] bytes = null;
        if (resource.exists()) {
            bytes = Resources.readToBytes(resource);
            if (bytes.length > CACHE_BYTES) {
                // do not cache
                return bytes;
            }
        }
        synchronized (fileCacheQueue) {
            if (fileCacheQueue.size() > CACHE_SIZE) {
                try {
                    Resource r = fileCacheQueue.take();
                    fileCacheMap.remove(r);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
            fileCacheQueue.add(resource);
            fileCacheMap.put(resource, new TimeControlBytes(bytes));
        }
        return bytes;
    }
}
