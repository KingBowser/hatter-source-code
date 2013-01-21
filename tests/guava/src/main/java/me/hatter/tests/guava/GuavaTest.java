package me.hatter.tests.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaTest {

    public static void main(String[] args) {
        Cache<String, Object> cache = CacheBuilder.newBuilder().build();
        cache.put("1", 1);
        cache.put("2", 2);

        System.out.println(cache.getIfPresent("1"));
    }
}
