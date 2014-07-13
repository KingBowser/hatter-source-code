package me.hatter.tools.commons.map;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import me.hatter.tools.commons.collection.IteratorTool;

public class CountMap<T> {

    private ConcurrentMap<T, AtomicLong> map = new ConcurrentHashMap<T, AtomicLong>();

    public long incrementAndGet(T key) {
        return countAndGet(key, 1L);
    }

    public long getAndIncrement(T key) {
        return getAndCount(key, 1L);
    }

    public long countAndGet(T key, long ct) {
        return newCount(key).addAndGet(ct);
    }

    public long getAndCount(T key, long ct) {
        return newCount(key).getAndAdd(ct);
    }

    public Set<T> keySet() {
        return map.keySet();
    }

    public IteratorTool<T> toKeySetIteratorTool() {
        return IteratorTool.from(map.keySet());
    }

    public Set<Entry<T, AtomicLong>> entrySet() {
        return map.entrySet();
    }

    public IteratorTool<Entry<T, AtomicLong>> toEntrySetIteratorTool() {
        return IteratorTool.from(map.entrySet());
    }

    public ConcurrentMap<T, AtomicLong> getOriMap() {
        return map;
    }

    public long getCount(T key) {
        AtomicLong al = map.get(key);
        return (al == null) ? 0L : al.longValue();
    }

    private AtomicLong newCount(T key) {
        AtomicLong al = map.get(key);
        if (al == null) {
            map.putIfAbsent(key, new AtomicLong(0));
            al = map.get(key);
        }
        return al;
    }
}
