package me.hatter.tools.commons.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.hatter.tools.commons.function.BiFunction;
import me.hatter.tools.commons.function.Filter;
import me.hatter.tools.commons.function.Function;
import me.hatter.tools.commons.function.IndexedProcedure;
import me.hatter.tools.commons.function.Procedure;

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
