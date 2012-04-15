package me.hatter.tools.resourceproxy.jsspserver.util;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import me.hatter.tools.resourceproxy.commons.util.FileUtil;

public class FileCacheManager {

    public static class TimeControlBytes {

        private long   cacheMills;
        private byte[] bytes;

        public TimeControlBytes(byte[] bytes) {
            this.cacheMills = System.currentTimeMillis();
            this.bytes = bytes;
        }
    }

    private static final long                  CACHE_BYTES    = 300 * 1024;
    private static final long                  CACHE_SIZE     = 1000;
    private static final long                  CACHE_MILLS    = TimeUnit.MINUTES.toMillis(5);
    private static BlockingQueue<File>         fileCacheQueue = new LinkedBlockingQueue<File>();
    private static Map<File, TimeControlBytes> fileCacheMap   = new WeakHashMap<File, TimeControlBytes>();

    public static byte[] readCacheFile(File file, AtomicBoolean isFromCache) {
        if (isFromCache != null) {
            isFromCache.set(false);
        }
        TimeControlBytes timeControlBytes = null;
        synchronized (fileCacheQueue) {
            timeControlBytes = fileCacheMap.get(file);
            if (timeControlBytes != null) {
                if ((timeControlBytes.cacheMills + CACHE_MILLS) < System.currentTimeMillis()) {
                    // timeout
                    fileCacheQueue.remove(file);
                    fileCacheMap.remove(file);
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
        if (file.exists()) {
            bytes = FileUtil.readFileToBytes(file);
            if (bytes.length > CACHE_BYTES) {
                // do not cache
                return bytes;
            }
        }
        synchronized (fileCacheQueue) {
            if (fileCacheQueue.size() > CACHE_SIZE) {
                try {
                    File f = fileCacheQueue.take();
                    fileCacheMap.remove(f);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
            fileCacheQueue.add(file);
            fileCacheMap.put(file, new TimeControlBytes(bytes));
        }
        return bytes;
    }
}
