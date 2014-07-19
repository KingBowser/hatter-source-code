package me.hatter.tools.commons.function;

public interface IndexedFilter<T> {

    boolean accept(T obj, int index);
}
