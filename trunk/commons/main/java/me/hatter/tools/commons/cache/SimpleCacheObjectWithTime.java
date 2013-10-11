package me.hatter.tools.commons.cache;

public class SimpleCacheObjectWithTime<T> implements CacheObjectWithTime<T> {

    private final T    object;
    private final long time;

    public SimpleCacheObjectWithTime(T object, long time) {
        super();
        this.object = object;
        this.time = time;
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public long getTime() {
        return time;
    }
}
