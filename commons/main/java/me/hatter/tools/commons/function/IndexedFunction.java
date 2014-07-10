package me.hatter.tools.commons.function;

public interface IndexedFunction<T, R> {

    R apply(T obj, int index);
}
