package me.hatter.tools.commons.function;

public class Filters {

    public static <T> Filter<T> objectNotNull() {
        return new NotNullFilter<T>();
    }

    public static Filter<String> stringNotEmpty() {
        return new StringNotEmpty();
    }

    public static <T> Filter<T> trueFilter() {
        return new TrueFilter<T>();
    }

    public static <T> Filter<T> falseFilter() {
        return new FalseFilter<T>();
    }

    // @SafeVarargs
    // @SuppressWarnings("unchecked")
    public static <T> Filter<T> andFilter(Filter<T>... filters) {
        return new AndFilter<T>(filters);
    }

    // @SafeVarargs
    // @SuppressWarnings("unchecked")
    public static <T> Filter<T> orFilter(Filter<T>... filters) {
        return new OrFilter<T>(filters);
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

    public static class TrueFilter<T> implements Filter<T> {

        public boolean accept(T object) {
            return true;
        }
    }

    public static class FalseFilter<T> implements Filter<T> {

        public boolean accept(T object) {
            return false;
        }
    }

    public static class AndFilter<T> implements Filter<T> {

        private Filter<T>[] filters;

        // @SafeVarargs
        // @SuppressWarnings("unchecked")
        public AndFilter(Filter<T>... filters) {
            this.filters = filters;
        }

        public boolean accept(T object) {
            if ((filters == null) || (filters.length == 0)) {
                return true;
            }
            for (Filter<T> filter : filters) {
                if (!filter.accept(object)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class OrFilter<T> implements Filter<T> {

        private Filter<T>[] filters;

        // @SafeVarargs
        // @SuppressWarnings("unchecked")
        public OrFilter(Filter<T>... filters) {
            this.filters = filters;
        }

        public boolean accept(T object) {
            if ((filters == null) || (filters.length == 0)) {
                return true;
            }
            for (Filter<T> filter : filters) {
                if (filter.accept(object)) {
                    return true;
                }
            }
            return false;
        }
    }
}
