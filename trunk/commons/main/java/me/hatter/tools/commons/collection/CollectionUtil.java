package me.hatter.tools.commons.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {

    public interface Filter<T> {

        boolean accept(T object);
    }

    public interface Transformer<T, D> {

        D transform(T object);
    }

    public interface KeyGetter<O, K> {

        K getKey(O object);
    }

    public static class NotNullFilter<T> implements Filter<T> {

        public boolean accept(T object) {
            return (object != null);
        }
    }

    public static class StringNotEmpty implements Filter<String> {

        public boolean accept(String object) {
            return ((object != null) && (!object.isEmpty()));
        }
    }

    public static class StringToUpperCase implements Transformer<String, String> {

        @Override
        public String transform(String object) {
            return (object == null) ? null : object.toUpperCase();
        }
    }

    public static class StringToLowerCase implements Transformer<String, String> {

        @Override
        public String transform(String object) {
            return (object == null) ? null : object.toLowerCase();
        }
    }

    public static class StringTrim implements Transformer<String, String> {

        @Override
        public String transform(String object) {
            return (object == null) ? null : object.trim();
        }
    }

    public static boolean isEmpty(Collection<?> collection) {
        return ((collection == null) || collection.isEmpty());
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static List<String> toUpperCase(Collection<String> list) {
        return transform(list, new CollectionUtil.StringToUpperCase());
    }

    public static List<String> toLowerCase(Collection<String> list) {
        return transform(list, new CollectionUtil.StringToLowerCase());
    }

    public static <T> List<T> add(Collection<T> list, Collection<T> addList) {
        List<T> newList = new ArrayList<T>();
        newList.addAll(list);
        newList.addAll(addList);
        return newList;
    }

    public static <T> List<T> minus(Collection<T> list, Collection<T> minusList) {
        final Set<T> minusSet = new HashSet<T>(minusList);
        return filter(list, new CollectionUtil.Filter<T>() {

            @Override
            public boolean accept(T object) {
                return (!minusSet.contains(object));
            }
        });
    }

    public static <T> List<T> filter(Collection<T> list, Filter<T> filter) {
        List<T> result = new ArrayList<T>();
        if (list != null) {
            for (T o : list) {
                if (filter == null) {
                    result.add(o);
                } else {
                    if (filter.accept(o)) {
                        result.add(o);
                    }
                }
            }
        }
        return result;
    }

    public static <T, D> List<D> transform(Collection<T> list, Transformer<T, D> transformer) {
        List<D> result = new ArrayList<D>();
        if (list != null) {
            for (T o : list) {
                result.add(transformer.transform(o));
            }
        }
        return result;
    }

    public static <O, K> Map<K, O> toMap(Collection<O> list, KeyGetter<O, K> keyGetter) {
        Map<K, O> result = new HashMap<K, O>();
        if (list != null) {
            for (O o : list) {
                result.put(keyGetter.getKey(o), o);
            }
        }
        return result;
    }

    public static <O, K, X> Map<K, X> toMap(Collection<O> list, KeyGetter<O, K> keyGetter, Transformer<O, X> transformer) {
        Map<K, X> result = new HashMap<K, X>();
        if (list != null) {
            for (O o : list) {
                result.put(keyGetter.getKey(o), transformer.transform(o));
            }
        }
        return result;
    }

    public static <O, K> Map<K, List<O>> toMapList(Collection<O> list, KeyGetter<O, K> keyGetter) {
        Map<K, List<O>> result = new HashMap<K, List<O>>();
        if (list != null) {
            for (O o : list) {
                K k = keyGetter.getKey(o);
                List<O> l = result.get(k);
                if (l == null) {
                    l = new ArrayList<O>();
                    result.put(k, l);
                }
                l.add(o);
            }
        }
        return result;
    }

    public static <O, K, X> Map<K, List<X>> toMapList(Collection<O> list, KeyGetter<O, K> keyGetter,
                                                      Transformer<O, X> transformer) {
        Map<K, List<X>> result = new HashMap<K, List<X>>();
        if (list != null) {
            for (O o : list) {
                K k = keyGetter.getKey(o);
                List<X> l = result.get(k);
                if (l == null) {
                    l = new ArrayList<X>();
                    result.put(k, l);
                }
                l.add(transformer.transform(o));
            }
        }
        return result;
    }

    public static <T> List<T> distinct(List<T> list) {
        if (list == null) {
            return list;
        }
        return new ArrayList<T>(new LinkedHashSet<T>(list));
    }

    public static <T> T firstObject(List<T> list) {
        return ((list == null) || list.isEmpty()) ? null : list.get(0);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> objectToList(T object) {
        return new ArrayList<T>(Arrays.asList(object));
    }

    public static <T> boolean contains(Collection<T> coll, T object) {
        if (coll == null) {
            return false;
        }
        return coll.contains(object);
    }

    public static <T> boolean containsAll(Collection<T> coll, Collection<T> sub) {
        if (coll == null) {
            return false;
        }
        if ((sub == null) || (sub.isEmpty())) {
            return true;
        }
        HashSet<T> coll2 = new HashSet<T>(coll);
        for (T object : sub) {
            if (!coll2.contains(object)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean containsAny(Collection<T> coll, Collection<T> sub) {
        if (coll == null) {
            return false;
        }
        if ((sub == null) || (sub.isEmpty())) {
            return true;
        }
        HashSet<T> coll2 = new HashSet<T>(coll);
        for (T object : sub) {
            if (coll2.contains(object)) {
                return true;
            }
        }
        return false;
    }

    public static List<Object> asList(Object obj, Object... objects) {
        List<Object> list = new ArrayList<Object>(1 + objects.length);
        list.add(obj);
        for (Object o : objects) {
            list.add(o);
        }
        return list;
    }
}