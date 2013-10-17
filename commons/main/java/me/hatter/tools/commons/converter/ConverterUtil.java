package me.hatter.tools.commons.converter;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
                        if (StringUtil.isNotEmpty(converterMark.separator())) {
                            String[] values = ((String) convert(o, String.class)).split(converterMark.separator());
                            for (String s : values) {
                                collection.add(convert(s, collectionClazz));
                            }
                        } else {
                            collection.add(convert(o, collectionClazz));
                        }

                    }
                } else {
                    if (StringUtil.isNotEmpty(converterMark.separator())) {
                        String[] values = ((String) convert(obj, String.class)).split(converterMark.separator());
                        for (String s : values) {
                            collection.add(convert(s, collectionClazz));
                        }
                    } else {
                        collection.add(convert(obj, collectionClazz));
                    }
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
        if ((clazz == String.class) || (clazz == CharSequence.class)) {
            return String.valueOf(obj);
        }
        if ((clazz == boolean.class) || (clazz == Boolean.class)) {
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            return Arrays.asList("1", "t", "on", "yes", "true").contains(StringUtil.toLowerCase(sval));
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
        if ((clazz == byte.class) || (clazz == Byte.class)) {
            if (obj instanceof Number) {
                return Byte.valueOf(((Number) obj).byteValue());
            }
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            return Byte.valueOf(sval);
        }
        if ((clazz == short.class) || (clazz == Short.class)) {
            if (obj instanceof Number) {
                return Short.valueOf(((Number) obj).shortValue());
            }
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            return Short.valueOf(sval);
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
        if (clazz == Date.class) {
            if (obj instanceof java.sql.Date) {
                return new Date(((java.sql.Date) obj).getTime());
            }
            if (obj instanceof java.sql.Time) {
                return new Date(((java.sql.Time) obj).getTime());
            }
            if (obj instanceof java.sql.Timestamp) {
                return new Date(((java.sql.Timestamp) obj).getTime());
            }
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            List<String> formats = Arrays.asList(null,//
                                                 "yyyy/MM/dd HH:mm:ss",//
                                                 "yyyy-MM-dd HH:mm:ss", //
                                                 "yyyy/MM/dd HH:mm",//
                                                 "yyyy-MM-dd HH:mm", //
                                                 "yyyy/MM/dd HH",//
                                                 "yyyy-MM-dd HH", //
                                                 "yyyy/MM/dd", //
                                                 "yyyy-MM-dd",//
                                                 null);
            for (String f : formats) {
                if (f != null) {
                    try {
                        return new SimpleDateFormat(f).parse(sval);
                    } catch (ParseException e) {
                        // DO NOTHING
                    }
                }
            }
            // if reaches here means convert fails
        }
        if (clazz == Locale.class) {
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            for (Locale locale : Locale.getAvailableLocales()) {
                if (locale.toString().equalsIgnoreCase(sval)) {
                    return locale;
                }
            }
            String[] slocales = sval.split("_");
            if (slocales.length == 1) {
                return new Locale(slocales[0]);
            }
            if (slocales.length == 2) {
                return new Locale(slocales[0], slocales[1]);
            }
            return new Locale(slocales[0], slocales[1], slocales[2]);
        }
        if (clazz == File.class) {
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            return new File(sval);
        }
        if (clazz == URL.class) {
            String sval = String.valueOf(obj);
            if (StringUtil.isBlank(sval)) {
                return null;
            }
            try {
                return new URL(sval);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
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
