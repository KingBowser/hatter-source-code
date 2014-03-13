package me.hatter.tools.commons.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

// INTERFACES ARE NOT EXACT
public class TimeSensitivityLRUCache<K, V> implements Map<K, V> {

    @SuppressWarnings("unused")
    private static final long                         serialVersionUID = 1L;
    private final LRUCache<K, CacheObjectWithTime<V>> lruCache;
    private final long                                maxLiveMillis;

    public TimeSensitivityLRUCache(int maxSize, long maxLiveMillis) {
        this.lruCache = new LRUCache<K, CacheObjectWithTime<V>>(maxSize);
        this.maxLiveMillis = maxLiveMillis;
    }

    public TimeSensitivityLRUCache(int maxSize, int initialCapacity, long maxLiveMillis) {
        this.lruCache = new LRUCache<K, CacheObjectWithTime<V>>(maxSize, initialCapacity);
        this.maxLiveMillis = maxLiveMillis;
    }

    public TimeSensitivityLRUCache(int maxSize, int initialCapacity, float loadFactor, boolean accessOrder,
                                   long maxLiveMillis) {
        this.lruCache = new LRUCache<K, CacheObjectWithTime<V>>(maxSize, initialCapacity, loadFactor, accessOrder);
        this.maxLiveMillis = maxLiveMillis;
    }

    @Override
    public int size() {
        return lruCache.size();
    }

    @Override
    public boolean isEmpty() {
        return lruCache.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (!lruCache.containsKey(key)) {
            return false;
        }
        CacheObjectWithTime<V> cacheObjectWithTime = lruCache.get(key);
        if (cacheObjectWithTime == null) {
            lruCache.remove(key);
            return false;
        }
        if (Math.abs(System.currentTimeMillis() - cacheObjectWithTime.getTime()) > maxLiveMillis) {
            lruCache.remove(key);
            return false;
        }
        return true;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(Object key) {
        CacheObjectWithTime<V> cacheObjectWithTime = lruCache.get(key);
        if (cacheObjectWithTime == null) {
            return null;
        }
        V object = getValidCacheObjectValue(cacheObjectWithTime);
        if (object == null) {
            lruCache.remove(key);
            return null;
        }
        return object;
    }

    @Override
    public V put(K key, V value) {
        CacheObjectWithTime<V> oldValue = lruCache.put(key,
                                                       new SimpleCacheObjectWithTime<V>(value,
                                                                                        System.currentTimeMillis()));
        return getValidCacheObjectValue(oldValue);
    }

    @Override
    public V remove(Object key) {
        CacheObjectWithTime<V> oldValue = lruCache.remove(key);
        return getValidCacheObjectValue(oldValue);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m != null) {
            for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void clear() {
        lruCache.clear();
    }

    @Override
    public Set<K> keySet() {
        return lruCache.keySet();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    private V getValidCacheObjectValue(CacheObjectWithTime<V> cacheObjectWithTime) {
        if (cacheObjectWithTime == null) {
            return null;
        }
        if (Math.abs(System.currentTimeMillis() - cacheObjectWithTime.getTime()) > maxLiveMillis) {
            return null;
        }
        return cacheObjectWithTime.getObject();
    }
}
