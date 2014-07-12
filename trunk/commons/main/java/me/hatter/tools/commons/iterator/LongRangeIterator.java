package me.hatter.tools.commons.iterator;

import java.util.Iterator;

import me.hatter.tools.commons.assertion.AssertUtil;
import me.hatter.tools.commons.collection.IteratorTool;

public class LongRangeIterator implements Iterator<Long> {

    private boolean initStat = true;
    private long    from;
    private long    to;
    private long    step;
    private long    current;

    public static LongRangeIterator from(long from, long to) {
        return new LongRangeIterator(from, to);
    }

    public static LongRangeIterator from(long from, long to, long step) {
        return new LongRangeIterator(from, to, step);
    }

    public LongRangeIterator(long from, long to) {
        this(from, to, (from < to) ? 1L : -1L);
    }

    public LongRangeIterator(long from, long to, long step) {
        AssertUtil.isTrue(from != to);
        AssertUtil.isTrue(step != 0L);
        if (from < to) {
            AssertUtil.isTrue(step > 0L);
        } else {
            AssertUtil.isTrue(step < 0L);
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
    public Long next() {
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

    public IteratorTool<Long> asIteratorTool() {
        return IteratorTool.from(this);
    }
}
