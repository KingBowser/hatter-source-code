package me.hatter.tools.commons.iterator;

import java.util.Iterator;

import me.hatter.tools.commons.assertion.AssertUtil;
import me.hatter.tools.commons.collection.IteratorTool;

public class RepeatIterator<T> implements Iterator<T> {

    private T   obj;
    private int count;
    private int index = 0;

    public static <T> RepeatIterator<T> from(T obj) {
        return new RepeatIterator<T>(obj);
    }

    public static <T> RepeatIterator<T> from(T obj, int count) {
        return new RepeatIterator<T>(obj, count);
    }

    public RepeatIterator(T obj) {
        this(obj, 1);
    }

    public RepeatIterator(T obj, int count) {
        AssertUtil.isTrue(count > 0);
        this.obj = obj;
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        return (index < count);
    }

    @Override
    public T next() {
        AssertUtil.isTrue(hasNext());
        index++;
        return obj;
    }

    @Override
    public void remove() {
        throw new IllegalStateException(this.getClass().getSimpleName() + " has no remove()");
    }

    public IteratorTool<T> asIteratorTool() {
        return IteratorTool.from(this);
    }
}
