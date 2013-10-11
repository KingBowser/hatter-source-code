package me.hatter.tools.resourceproxy.jsspserver.util;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import me.hatter.tools.commons.resource.Resource;
import me.hatter.tools.commons.resource.Resources;

public class ResourceCacheManager {

    public static class TimeControlBytes {

        private long   cacheMills;
        private byte[] bytes;

        public TimeControlBytes(byte[] bytes) {
            this.cacheMills = System.currentTimeMillis();
            this.bytes = bytes;
        }
    }

    private static final long                      CACHE_BYTES        = 300 * 1024;
    private static final long                      CACHE_SIZE         = 1000;
    private static final long                      CACHE_MILLS        = TimeUnit.MINUTES.toMillis(5);
    private static BlockingQueue<Resource>         resourceCacheQueue = new LinkedBlockingQueue<Resource>();
    private static Map<Resource, TimeControlBytes> resourceCacheMap   = new WeakHashMap<Resource, TimeControlBytes>();

    public static byte[] readCacheFile(Resource resource, AtomicBoolean isFromCache) {
        if (isFromCache != null) {
            isFromCache.set(false);
        }
        TimeControlBytes timeControlBytes = null;
        synchronized (resourceCacheQueue) {
            timeControlBytes = resourceCacheMap.get(resource);
            if (timeControlBytes != null) {
                if ((timeControlBytes.cacheMills + CACHE_MILLS) < System.currentTimeMillis()) {
                    // timeout
                    resourceCacheQueue.remove(resource);
                    resourceCacheMap.remove(resource);
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
        synchronized (resourceCacheQueue) {
            if (resourceCacheQueue.size() > CACHE_SIZE) {
                try {
                    Resource r = resourceCacheQueue.take();
                    resourceCacheMap.remove(r);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
            resourceCacheQueue.add(resource);
            resourceCacheMap.put(resource, new TimeControlBytes(bytes));
        }
        return bytes;
    }
}
