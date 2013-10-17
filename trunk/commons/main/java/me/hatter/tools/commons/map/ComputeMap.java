package me.hatter.tools.commons.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class ComputeMap<K, V> implements Map<K, V> {

    private ConcurrentMap<K, V> map = new ConcurrentHashMap<K, V>();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        if (map.containsKey(key)) {
            V value = map.get(key);
            if (value != null) {
                return value;
            }
            if ((value == null) && (map.containsKey(key))) {
                return value;
            }
        }
        synchronized (map) {
            if (map.containsKey(key)) {
                V value = map.get(key);
                if (value != null) {
                    return value;
                }
                if ((value == null) && (map.containsKey(key))) {
                    return value;
                }
            }

            // KEEP single instance
            V value = compute((K) key);
            map.put((K) key, value);
            return value;
        }
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    abstract protected V compute(K key);
}
