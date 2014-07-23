package me.hatter.tools.commons.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import me.hatter.tools.commons.assertion.AssertUtil;
import me.hatter.tools.commons.function.BiFunction;
import me.hatter.tools.commons.function.Filter;
import me.hatter.tools.commons.function.Function;
import me.hatter.tools.commons.function.IndexedFilter;
import me.hatter.tools.commons.function.IndexedFunction;
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
        this(iterable.iterator());
    }

    // ========================================================================

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

    public IteratorTool<T> filter(final IndexedFilter<T> filter) {
        final List<T> list = new ArrayList<T>();
        each(new IndexedProcedure<T>() {

            @Override
            public void apply(T obj, int index) {
                if (filter.accept(obj, index)) {
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

    public <U> IteratorTool<U> map(final IndexedFunction<T, U> func) {
        final List<U> list = new ArrayList<U>();
        each(new IndexedProcedure<T>() {

            @Override
            public void apply(T obj, int index) {
                list.add(func.apply(obj, index));
            }
        });
        return from(list);
    }

    public IteratorTool<T> process(final Procedure<T> procedure) {
        final List<T> list = new ArrayList<T>();
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                procedure.apply(obj);
                list.add(obj);
            }
        });
        return from(list);
    }

    public IteratorTool<T> process(final IndexedProcedure<T> indexedProcedure) {
        final List<T> list = new ArrayList<T>();
        each(new IndexedProcedure<T>() {

            @Override
            public void apply(T obj, int index) {
                indexedProcedure.apply(obj, index);
                list.add(obj);
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

    public Integer sumAsInteger() {
        return (Integer) reduce(new BiFunction<T, T, T>() {

            @Override
            @SuppressWarnings("unchecked")
            public T apply(T objT, T objU) {
                Integer a = (objT == null) ? 0 : ((Integer) objT);
                Integer b = (objU == null) ? 0 : ((Integer) objU);
                return (T) (Integer) (a + b);
            }
        });
    }

    public Long sumAsLong() {
        return (Long) reduce(new BiFunction<T, T, T>() {

            @Override
            @SuppressWarnings("unchecked")
            public T apply(T objT, T objU) {
                Long a = (objT == null) ? 0L : ((Long) objT);
                Long b = (objU == null) ? 0L : ((Long) objU);
                return (T) (Long) (a + b);
            }
        });
    }

    public String join(String separator) {
        return join(separator, false, "");
    }

    public String join(final String separator, final boolean skipEmpty, final String defaultString) {
        final StringBuilder sb = new StringBuilder();
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                String s = (obj == null) ? null : obj.toString();
                if ((s == null) || s.isEmpty()) {
                    if (skipEmpty) {
                        // DO NOTHING
                    } else {
                        if (sb.length() > 0) {
                            sb.append(separator);
                        }
                        sb.append(defaultString);
                    }
                    return;
                }
                if (sb.length() > 0) {
                    sb.append(separator);
                }
                sb.append(s);
            }
        });
        return sb.toString();
    }

    public T[] array(Class<? extends T[]> clazz) {
        return asArray(clazz);
    }

    public T[] asArray(Class<? extends T[]> clazz) {
        List<T> list = (List<T>) list();
        return (T[]) Arrays.copyOf(list.toArray(), list.size(), clazz);
    }

    public Object[] array() {
        return asArray();
    }

    public Object[] asArray() {
        return list().toArray();
    }

    public List<T> list() {
        return asList();
    }

    public List<T> asList() {
        return (List<T>) fillCollection(new ArrayList<T>());
    }

    public LinkedList<T> asLinkedList() {
        return (LinkedList<T>) fillCollection(new LinkedList<T>());
    }

    public Set<T> set() {
        return asSet();
    }

    public Set<T> asSet() {
        return (Set<T>) fillCollection(new HashSet<T>());
    }

    public LinkedHashSet<T> asLinkedSet() {
        return (LinkedHashSet<T>) fillCollection(new LinkedHashSet<T>());
    }

    public TreeSet<T> asTreeSet() {
        return (TreeSet<T>) fillCollection(new TreeSet<T>());
    }

    public Collection<T> fillCollection(final Collection<T> collection) {
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                collection.add(obj);
            }
        });
        return collection;
    }

    public IteratorTool<T> reverse() {
        List<T> list = asList();
        for (int i = 0; i < (list.size() / 2); i++) {
            T a = list.get(i);
            T b = list.get(list.size() - i - 1);
            list.set(i, b);
            list.set(list.size() - i - 1, a);
        }
        return from(list);
    }

    public IteratorTool<T> distinct() {
        return IteratorTool.from(asSet());
    }

    public IteratorTool<T> distinctOrdered() {
        return IteratorTool.from(asLinkedSet());
    }

    public IteratorTool<T> head(final int count) {
        AssertUtil.isTrue(count >= 0);
        return filter(new IndexedFilter<T>() {

            @Override
            public boolean accept(T obj, int index) {
                return (index < count);
            }
        });
    }

    public IteratorTool<T> top(final int count) {
        return head(count);
    }

    public IteratorTool<T> skip(final int count) {
        AssertUtil.isTrue(count >= 0);
        return filter(new IndexedFilter<T>() {

            @Override
            public boolean accept(T obj, int index) {
                return (index >= count);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public IteratorTool<T> tail(final int count) {
        AssertUtil.isTrue(count >= 0);
        if (count == 0) {
            return from(new ArrayList<T>());
        }
        final Object[] array = new Object[count];
        final AtomicInteger i = new AtomicInteger(0);
        final AtomicInteger realCount = new AtomicInteger(0);
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                if (i.get() >= count) {
                    i.set(0);
                }
                array[i.getAndIncrement()] = obj;
                realCount.incrementAndGet();
            }
        });
        List<T> list = new ArrayList<T>(count);
        if (realCount.get() < count) { // skip null elements
            i.set(i.get() + count - realCount.get());
        }
        for (int x = 0; x < Math.min(count, realCount.get()); x++) {
            if (i.get() >= count) {
                i.set(0);
            }
            list.add((T) array[i.getAndIncrement()]);
        }

        return from(list);
    }

    public IteratorTool<T> index(final int... indexes) {
        return filter(new IndexedFilter<T>() {

            @Override
            public boolean accept(Object obj, int index) {
                for (int i : indexes) {
                    if (i == index) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public IteratorTool<T> asSorted() {
        List list = (List) asList();
        AssertUtil.isTrue((list.get(0) == null) || (list.get(0) instanceof Comparable));
        Collections.sort((List<Comparable>) list);
        return IteratorTool.from((List<T>) list);
    }

    public IteratorTool<T> asSorted(Comparator<T> comparator) {
        List<T> list = asList();
        Collections.sort(list, comparator);
        return IteratorTool.from(list);
    }

    public CountMap<T> toCountMap() {
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

    public void print() {
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                System.out.println(obj);
            }
        });
    }

    public void print(final Function<T, String> function) {
        each(new Procedure<T>() {

            @Override
            public void apply(T obj) {
                System.out.println(function.apply(obj));
            }
        });
    }

    public void print(final IndexedFunction<T, String> indexedFunction) {
        each(new IndexedProcedure<T>() {

            @Override
            public void apply(T obj, int index) {
                System.out.println(indexedFunction.apply(obj, index));
            }
        });
    }
}
