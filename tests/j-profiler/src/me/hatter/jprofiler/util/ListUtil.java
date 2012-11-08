package me.hatter.jprofiler.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static interface Filter<T> {

        boolean accept(T object);
    }

    public static <T> List<T> filter(List<T> list, Filter<T> filter) {
        List<T> filterList = new ArrayList<T>();
        if (list != null) {
            for (T object : list) {
                if (filter.accept(object)) {
                    filterList.add(object);
                }
            }
        }
        return filterList;
    }

    public static <T> List<T> notNull(List<T> list) {
        return (list == null) ? new ArrayList<T>() : list;
    }

    public static <T> List<T> reverse(List<T> list) {
        if (list == null) {
            return null;
        }
        List<T> reverseList = new ArrayList<T>();
        for (int i = (list.size() - 1); i >= 0; i--) {
            reverseList.add(list.get(i));
        }
        return reverseList;
    }
}
