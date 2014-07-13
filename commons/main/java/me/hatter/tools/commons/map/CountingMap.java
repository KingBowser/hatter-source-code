package me.hatter.tools.commons.map;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

// use CountMap instead
@Deprecated
public class CountingMap {

    private ConcurrentMap<String, AtomicLong> map = new ConcurrentHashMap<String, AtomicLong>();

    public long countAndGet(String key, long ct) {
        return newCount(key).addAndGet(ct);
    }

    public long getAndCount(String key, long ct) {
        return newCount(key).getAndAdd(ct);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public ConcurrentMap<String, AtomicLong> getOriMap() {
        return map;
    }

    public long getCount(String key) {
        AtomicLong al = map.get(key);
        return (al == null) ? 0L : al.longValue();
    }

    private AtomicLong newCount(String key) {
        AtomicLong al = map.get(key);
        if (al == null) {
            map.putIfAbsent(key, new AtomicLong(0));
            al = map.get(key);
        }
        return al;
    }
}
