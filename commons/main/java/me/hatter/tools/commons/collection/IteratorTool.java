package me.hatter.tools.commons.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import me.hatter.tools.commons.assertion.AssertUtil;
import me.hatter.tools.commons.function.BiFunction;
import me.hatter.tools.commons.function.Filter;
import me.hatter.tools.commons.function.Function;
import me.hatter.tools.commons.function.IndexedProcedure;
import me.hatter.tools.commons.function.Procedure;
import me.hatter.tools.commons.map.CountMap;

public class IteratorTool<T> {

    private Iterator<T> iterator;

    public static <T> IteratorTool<T> from(Iterator<T> iterator) {
        return new IteratorTool<T>(iterator);
    }

    public static <T> IteratorTool<T> from(Iterable<T> iterable) {
        return new IteratorTool<T>(iterable);
    }

    public IteratorTool(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public IteratorTool(Iterable<T> iterable) {
        this.iterator = iterable.iterator();
    }

    public IteratorTool<T> filter(final Filter<T> filter) {
        final List<T> list = new ArrayList<T>();
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                if (filter.accept(obj)) {
                    list.add(obj);
                }
            }
        });
        return from(list);
    }

    public <U> IteratorTool<U> map(final Function<T, U> func) {
        final List<U> list = new ArrayList<U>();
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                list.add(func.apply(obj));
            }
        });
        return from(list);
    }

    public T reduce(final BiFunction<T, T, T> biFunc) {
        T t = iterator.hasNext() ? iterator.next() : null;
        while (iterator.hasNext()) {
            t = biFunc.apply(t, iterator.next());
        }
        return t;
    }

    public List<T> asList() {
        final List<T> list = new ArrayList<T>();
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                list.add(obj);
            }
        });
        return list;
    }

    public Set<T> asSet() {
        final Set<T> set = new HashSet<T>();
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                set.add(obj);
            }
        });
        return set;
    }

    public IteratorTool<T> distinct() {
        final LinkedHashSet<T> set = new LinkedHashSet<T>();
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                set.add(obj);
            }
        });
        return IteratorTool.from(set);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public IteratorTool<T> asSorted() {
        List list = (List) asList();
        AssertUtil.isTrue(list.get(0) instanceof Comparator);
        Collections.sort((List<Comparable>) list);
        return IteratorTool.from((List<T>) list);
    }

    public IteratorTool<T> asSorted(Comparator<T> comparator) {
        List<T> list = asList();
        Collections.sort(list, comparator);
        return IteratorTool.from(list);
    }

    public CountMap<T> count() {
        final CountMap<T> countMap = new CountMap<T>();
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                countMap.incrementAndGet(obj);
            }
        });
        return countMap;
    }

    public void each(Procedure<T> procedure) {
        while (iterator.hasNext()) {
            procedure.apply(iterator.next());
        }
    }

    public void each(IndexedProcedure<T> indexedProcedure) {
        for (int i = 0; iterator.hasNext(); i++) {
            indexedProcedure.apply(iterator.next(), i);
        }
    }
}
