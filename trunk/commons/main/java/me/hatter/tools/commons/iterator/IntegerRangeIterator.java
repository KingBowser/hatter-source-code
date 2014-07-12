package me.hatter.tools.commons.iterator;

import java.util.Iterator;

import me.hatter.tools.commons.assertion.AssertUtil;
import me.hatter.tools.commons.collection.IteratorTool;

public class IntegerRangeIterator implements Iterator<Integer> {

    private boolean initStat = true;
    private int     from;
    private int     to;
    private int     step;
    private int     current;

    public static IntegerRangeIterator from(int from, int to) {
        return new IntegerRangeIterator(from, to);
    }

    public static IntegerRangeIterator from(int from, int to, int step) {
        return new IntegerRangeIterator(from, to, step);
    }

    public IntegerRangeIterator(int from, int to) {
        this(from, to, (from < to) ? 1 : -1);
    }

    public IntegerRangeIterator(int from, int to, int step) {
        AssertUtil.isTrue(from != to);
        AssertUtil.isTrue(step != 0);
        if (from < to) {
            AssertUtil.isTrue(step > 0);
        } else {
            AssertUtil.isTrue(step < 0);
        }
        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public boolean hasNext() {
        if (initStat) {
            return true;
        }
        if (from < to) {
            return ((current + step) <= to);
        } else {
            return (current + step >= to);
        }
    }

    @Override
    public Integer next() {
        AssertUtil.isTrue(hasNext());
        if (initStat) {
            initStat = false;
            current = from;
            return from;
        }
        current = current + step;
        return current;
    }

    @Override
    public void remove() {
        throw new IllegalStateException(this.getClass().getSimpleName() + " has no remove()");
    }

    public IteratorTool<Integer> asIteratorTool() {
        return IteratorTool.from(this);
    }
}
