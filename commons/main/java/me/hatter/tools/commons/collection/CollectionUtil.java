package me.hatter.tools.commons.collection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import me.hatter.tools.commons.assertion.AssertUtil;
import me.hatter.tools.commons.function.Filter;
import me.hatter.tools.commons.function.Filters.AndFilter;
import me.hatter.tools.commons.function.Filters.OrFilter;
import me.hatter.tools.commons.reflect.ReflectUtil;
import me.hatter.tools.commons.string.StringUtil;

public class CollectionUtil {

    public interface Transformer<T, D> {

        D transform(T object);
    }

    public interface KeyGetter<O, K> {

        K getKey(O object);
    }

    public static class Group<T> extends ArrayList<T> {

        private static final long serialVersionUID = 1L;

        public String join(String separator) {
            return StringUtil.join(this.toArray(), separator);
        }
    }

    public static class Groups<T> extends Group<Group<T>> {

        private static final long serialVersionUID = 1L;
    }

    public static class Transformers {

        public static Transformer<String, String> stringToUpperCase() {
            return new StringToUpperCase();
        }

        public static Transformer<String, String> stringToLowerCase() {
            return new StringToLowerCase();
        }

        public static Transformer<String, String> stringTrim() {
            return new StringTrim();
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

    public static class FieldKeyGetter<O, K> implements KeyGetter<O, K> {

        private String fieldName;

        public FieldKeyGetter(String fieldName) {
            this.fieldName = fieldName;
        }

        @SuppressWarnings("unchecked")
        @Override
        public K getKey(O object) {
            if (object == null) return null;
            Field field = ReflectUtil.getDeclaredField(object.getClass(), fieldName);
            ReflectUtil.makeAccessiable(field);
            try {
                return (K) field.get(object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> IteratorTool<T> it(Iterator<T> iterator) {
        return new IteratorTool<T>(iterator);
    }

    public static <T> IteratorTool<T> it(Iterable<T> iterable) {
        return new IteratorTool<T>(iterable);
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

    public static <T> Collection<T> emptyToNull(Collection<T> coll) {
        return (isEmpty(coll)) ? null : coll;
    }

    public static <T> Collection<T> notNull(Collection<T> coll) {
        return (coll == null) ? new ArrayList<T>(0) : coll;
    }

    public static <T> int size(Collection<T> coll) {
        return (coll == null) ? 0 : coll.size();
    }

    public static <T> List<T> add(Collection<T> list, Collection<T> addList) {
        List<T> newList = new ArrayList<T>(size(list) + size(addList));
        if (list != null) {
            newList.addAll(list);
        }
        if (addList != null) {
            newList.addAll(addList);
        }
        return newList;
    }

    public static <T> List<T> minus(Collection<T> list, Collection<T> minusList) {
        final Set<T> minusSet = new HashSet<T>(minusList);
        return filter(list, new Filter<T>() {

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

    // @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> List<T> filterAll(Collection<T> list, Filter<T>... filters) {
        return filter(list, new AndFilter<T>(filters));
    }

    // @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> List<T> filterAny(Collection<T> list, Filter<T>... filters) {
        return filter(list, new OrFilter<T>(filters));
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

    // @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> List<T> transformAll(Collection<T> list, Transformer<T, T>... transformers) {
        if ((transformers != null) && (transformers.length > 0)) {
            for (Transformer<T, T> transformer : transformers) {
                list = transform(list, transformer);
            }
        }
        return toList(list);
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

    public static <T> List<T> repeat(T obj, int count) {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < count; i++) {
            list.add(obj);
        }
        return list;
    }

    @Deprecated
    public static <T> Set<T> collectionAsSet(Collection<T> coll) {
        return toSet(coll);
    }

    @Deprecated
    public static <T> List<T> collectionAsList(Collection<T> coll) {
        return toList(coll);
    }

    // -- Set ---
    public static <T> Set<T> toSet(Collection<T> coll) {
        if (coll == null) {
            return null;
        }
        return (coll instanceof Set) ? ((Set<T>) coll) : new HashSet<T>(coll);
    }

    public static <T> HashSet<T> toHashSet(Collection<T> coll) {
        if (coll == null) {
            return null;
        }
        return (coll instanceof HashSet) ? ((HashSet<T>) coll) : new HashSet<T>(coll);
    }

    public static <T> TreeSet<T> toTreeSet(Collection<T> coll) {
        if (coll == null) {
            return null;
        }
        return (coll instanceof TreeSet) ? ((TreeSet<T>) coll) : new TreeSet<T>(coll);
    }

    // @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> Set<T> asSet(T obj, T... objects) {
        Set<T> set = new HashSet<T>(1 + objects.length);
        set.add(obj);
        for (T o : objects) {
            set.add(o);
        }
        return set;
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> asTreeSet(T obj, T... objects) {
        Set<T> set = new TreeSet<T>();
        set.add(obj);
        for (T o : objects) {
            set.add(o);
        }
        return set;
    }

    // -- List ---
    public static <T> List<T> toList(Collection<T> coll) {
        if (coll == null) {
            return null;
        }
        return (coll instanceof List) ? ((List<T>) coll) : new ArrayList<T>(coll);
    }

    public static <T> ArrayList<T> toArrayList(Collection<T> coll) {
        if (coll == null) {
            return null;
        }
        return (coll instanceof ArrayList) ? ((ArrayList<T>) coll) : new ArrayList<T>(coll);
    }

    public static <T> LinkedList<T> toLinkedList(Collection<T> coll) {
        if (coll == null) {
            return null;
        }
        return (coll instanceof LinkedList) ? ((LinkedList<T>) coll) : new LinkedList<T>(coll);
    }

    // @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> List<T> asList(T obj, T... objects) {
        List<T> list = new ArrayList<T>(1 + objects.length);
        list.add(obj);
        for (T o : objects) {
            list.add(o);
        }
        return list;
    }

    public static <T> Groups<T> split(Collection<T> list, int groupSize) {
        return split(list, groupSize, false);
    }

    public static <T> Groups<T> split(Collection<T> list, int groupSize, boolean skipNull) {
        AssertUtil.isTrue(groupSize > 0);
        Groups<T> groups = new Groups<T>();
        Group<T> group = new Group<T>();
        if (list != null) {
            for (T obj : list) {
                if (skipNull && (obj == null)) {
                    continue;
                }
                if (group.size() == groupSize) {
                    groups.add(group);
                    group = new Group<T>();
                }
                group.add(obj);
            }
            if (!group.isEmpty()) {
                groups.add(group);
            }
        }
        return groups;
    }
}
