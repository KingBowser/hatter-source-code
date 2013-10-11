package me.hatter.tools.commons.cache;

public interface CacheObjectWithTime<T> extends CacheObject<T> {

    long getTime();
}
