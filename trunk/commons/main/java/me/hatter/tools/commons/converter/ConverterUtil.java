package me.hatter.tools.commons.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.string.StringUtil;

public class ConverterUtil {

    private static ConcurrentMap<Class<?>, Converter> converterMap = new ConcurrentHashMap<Class<?>, Converter>();

    public static void register(Class<?> clazz, Converter converter) {
        converterMap.put(clazz, converter);
    }

    public static boolean isClassMultiple(Class<?> clazz) {
        return (clazz.isArray()) || (Collection.class.isAssignableFrom(clazz));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object convertToFit(Object obj, Field field) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            throw new RuntimeException("Cannot support obj type is array: " + obj.getClass());
        }
        Class<?> clazz = field.getType();
        if (isClassMultiple(clazz)) {
            if (clazz.isArray()) {
                throw new RuntimeException("Class is array not supported: " + clazz);
            } else {
                ConverterMark converterMark = field.getAnnotation(ConverterMark.class);
                Class<?> collectionClazz = ((converterMark == null) ? Object.class : converterMark.type());
                Collection collection = creatCollectionByClass(clazz);
                if (obj instanceof Collection) {
                    Collection<?> coll = (Collection<?>) obj;
                    for (Object o : coll) {
                        collection.add(convert(o, collectionClazz));
                    }
                } else {
                    collection.add(convert(obj, collectionClazz));
                }
                return collection;
            }
        } else {
            return convert(obj, clazz);
        }
    }

    public static Object convert(Object obj, Class<?> clazz) {
        if (obj == null) {
            return null;
        }
        if (clazz == Object.class) {
            return obj;
        }
        if (obj.getClass() == clazz) {
            return obj;
        }
        Converter converter = converterMap.get(clazz);
        if (converter != null) {
            return converter.convert(obj);
        }
        // buildin
        if (clazz == String.class) {
            return String.valueOf(obj);
        }
        if ((clazz == boolean.class) || (clazz == Boolean.class)) {
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            return Arrays.asList("1", "on", "yes", "true").contains(StringUtil.toLowerCase(String.valueOf(obj)));
        }
        if ((clazz == int.class) || (clazz == Integer.class)) {
            if (obj instanceof Number) {
                return Integer.valueOf(((Number) obj).intValue());
            }
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            return Integer.valueOf(sval);
        }
        if ((clazz == long.class) || (clazz == Long.class)) {
            if (obj instanceof Number) {
                return Long.valueOf(((Number) obj).longValue());
            }
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            return Long.valueOf(sval);
        }
        if ((clazz == float.class) || (clazz == Float.class)) {
            if (obj instanceof Number) {
                return Float.valueOf(((Number) obj).floatValue());
            }
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            return Float.valueOf(sval);
        }
        if ((clazz == double.class) || (clazz == Double.class)) {
            if (obj instanceof Number) {
                return Double.valueOf(((Number) obj).doubleValue());
            }
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            return Double.valueOf(sval);
        }

        throw new RuntimeException("Cannot convert value: '" + obj + "' to class: " + clazz);
    }

    @SuppressWarnings("rawtypes")
    private static Collection<?> creatCollectionByClass(Class<?> clazz) {
        if (!Collection.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("Class not implement Collection: " + clazz);
        }
        if (!Modifier.isAbstract(clazz.getModifiers())) {
            try {
                return (Collection<?>) clazz.newInstance();
            } catch (Exception e) {
                throw ExceptionUtil.wrapRuntimeException(e);
            }
        }
        if (List.class.isAssignableFrom(clazz)) {
            return new ArrayList();
        }
        if (Set.class.isAssignableFrom(clazz)) {
            return new HashSet();
        }
        return new ArrayList();
    }
}
