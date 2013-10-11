package me.hatter.tools.commons.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 1L;
    private final int         maxSize;

    public LRUCache(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    public LRUCache(int maxSize, int initialCapacity) {
        super(initialCapacity);
        this.maxSize = maxSize;
    }

    public LRUCache(int maxSize, int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxSize = maxSize;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.maxSize;
    }
}
