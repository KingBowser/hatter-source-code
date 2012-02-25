package me.hatter.tools.resourceproxy.dbutils.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
}
