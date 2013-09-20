package me.hatter.tools.resourceproxy.commons.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollUtil {

    public interface Filter<T> {

        boolean accept(T object);
    }

    public interface Transformer<T, D> {

        D transform(T object);
    }

    public class NotNullFilter<T> implements Filter<T> {

        public boolean accept(T object) {
            return (object != null);
        }
    }

    public static class StringToUpperCase implements Transformer<String, String> {

        // @Override
        public String transform(String object) {
            return (object == null) ? null : object.toUpperCase();
        }
    }

    public static class StringToLowerCase implements Transformer<String, String> {

        // @Override
        public String transform(String object) {
            return (object == null) ? null : object.toLowerCase();
        }
    }

    public static List<String> toUpperCase(Collection<String> list) {
        return transform(list, new CollUtil.StringToUpperCase());
    }

    public static List<String> toLowerCase(Collection<String> list) {
        return transform(list, new CollUtil.StringToLowerCase());
    }

    public static <T> List<T> minus(Collection<T> list, Collection<T> minusList) {
        final Set<T> minusSet = new HashSet<T>(minusList);
        return filter(list, new CollUtil.Filter<T>() {

            // @Override
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

    public static <T> T firstObject(List<T> list) {
        return ((list == null) || list.isEmpty()) ? null : list.get(0);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> objectToList(T object) {
        return new ArrayList<T>(Arrays.asList(object));
    }
}
