package me.hatter.tools.commons.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.collection.CollectionUtil.KeyGetter;
import me.hatter.tools.commons.converter.ConverterUtil;
import me.hatter.tools.commons.exception.ExceptionUtil;

public class ReflectUtil {

    public static interface ValueGetter {

        String getValue(String key);

        List<String> getValues(String key);
    }

    private static final Map<String, Boolean> classPresentMap = new HashMap<String, Boolean>();

    public static boolean isClassPresent(String className) {
        synchronized (classPresentMap) {
            if (classPresentMap.get(className) != null) {
                return classPresentMap.get(className).booleanValue();
            }
        }
        boolean found = true;
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            found = false;
        }
        synchronized (classPresentMap) {
            classPresentMap.put(className, Boolean.valueOf(found));
        }
        return found;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> interf) {
        Class<?> clazz;
        boolean isClazz = (obj instanceof Class);
        if (obj instanceof Class) {
            clazz = (Class<?>) obj;
        } else {
            clazz = obj.getClass();
        }
        final Object fObj = obj;
        final Class<?> fClazz = clazz;
        final boolean fIsClazz = isClazz;

        return (T) Proxy.newProxyInstance(interf.getClassLoader(), new Class<?>[] { interf }, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Method m = getDeclaredMethod(fClazz, method.getName(), method.getParameterTypes());
                if (m == null) {
                    throw new RuntimeException("Method '" + fClazz.getName() + "." + method.getName() + "("
                                               + method.getParameterTypes() + ")' not found.");
                }
                if (!m.isAccessible()) m.setAccessible(true);
                return m.invoke((fIsClazz ? null : fObj), args);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> interf, final Object filter) {
        Class<?> clazz;
        boolean isClazz = (obj instanceof Class);
        if (obj instanceof Class) {
            clazz = (Class<?>) obj;
        } else {
            clazz = obj.getClass();
        }
        final Object fObj = obj;
        final Class<?> fClazz = clazz;
        final boolean fIsClazz = isClazz;

        return (T) Proxy.newProxyInstance(interf.getClassLoader(), new Class<?>[] { interf }, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, final Object[] args) throws Throwable {
                Method mw = getDeclaredMethod(filter.getClass(), "____filter", new Class<?>[] { Object[].class,
                        Callable.class });
                Method mf = getDeclaredMethod(filter.getClass(), method.getName(), method.getParameterTypes());

                final Method m = ((mf != null) ? mf : getDeclaredMethod(fClazz, method.getName(),
                                                                        method.getParameterTypes()));
                if (m == null) {
                    throw new RuntimeException("Method '" + fClazz.getName() + "." + method.getName() + "("
                                               + method.getParameterTypes() + ")' not found.");
                }
                if (!m.isAccessible()) m.setAccessible(true);
                if (mw == null) {
                    return m.invoke((fIsClazz ? null : fObj), args);
                } else {
                    if (!mw.isAccessible()) mw.setAccessible(true);
                    return mw.invoke(filter, new Object[] { args, new Callable<Object>() {

                        public Object call() throws Exception {
                            return m.invoke((fIsClazz ? null : fObj), args);
                        }
                    } });
                }
            }
        });
    }

    public static <T> T copyToObject(Object src, Class<T> clazz) {
        try {
            T dest = clazz.newInstance();
            copyObjectFields(src, dest);
            return dest;
        } catch (Exception e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    public static void copyObjectFields(Object src, Object dest) {
        List<Field> srcFields = getDeclaredFields(src.getClass());
        List<Field> destFields = getDeclaredFields(dest.getClass());

        Map<String, Field> srcFieldMap = CollectionUtil.toMap(srcFields, new KeyGetter<Field, String>() {

            @Override
            public String getKey(Field object) {
                return object.getName();
            }
        });
        Map<String, Field> destFieldMap = CollectionUtil.toMap(destFields, new KeyGetter<Field, String>() {

            @Override
            public String getKey(Field object) {
                return object.getName();
            }
        });

        for (Entry<String, Field> srcFieldMapEntry : srcFieldMap.entrySet()) {
            Field srcField = srcFieldMapEntry.getValue();
            Field destField = destFieldMap.get(srcFieldMapEntry.getKey());
            if (destField != null) {
                makeAccessiable(srcField);
                makeAccessiable(destField);
                try {
                    destField.set(dest, ConverterUtil.convertToFit(srcField.get(src), destField));
                } catch (Exception e) {
                    throw ExceptionUtil.wrapRuntimeException(e);
                }
            }
        }
    }

    public static void makeAccessiable(Field field) {
        if ((field != null) && (!field.isAccessible())) {
            field.setAccessible(true);
        }
    }

    public static void makeAccessiable(Method method) {
        if ((method != null) && (!method.isAccessible())) {
            method.setAccessible(true);
        }
    }

    public static void makeAccessiable(Constructor<?> constructor) {
        if ((constructor != null) && (!constructor.isAccessible())) {
            constructor.setAccessible(true);
        }
    }

    public static Class<?> classForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            return method;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            if (clazz == Object.class) {
                return null;
            } else {
                return getDeclaredMethod(clazz.getSuperclass(), methodName, parameterTypes);
            }
        }
    }

    public static <T> T parse(ValueGetter getter, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            return fill(getter, obj);
        } catch (Exception e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    public static <T> T fill(ValueGetter getter, T obj) {
        try {
            List<Field> fields = ReflectUtil.getDeclaredFields(obj.getClass());
            for (Field field : fields) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                String fn = field.getName();
                Object oriVal = (ConverterUtil.isClassMultiple(field.getType())) ? getter.getValues(fn) : getter.getValue(fn);
                Object val = ConverterUtil.convertToFit(oriVal, field);
                if (val != null) {
                    field.set(obj, val);
                }
            }
            return obj;
        } catch (Exception e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    public static Object getFieldValue(Field field, Object object) {
        makeAccessiable(field);
        try {
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeMethod(Method method, Object object, Object... args) {
        makeAccessiable(method);
        try {
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getDeclaredField(Class<?> clazz, String field) {
        if (clazz == null) {
            return null;
        }
        if (clazz == Object.class) {
            return null;
        }
        try {
            return clazz.getDeclaredField(field);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            return getDeclaredField(clazz.getSuperclass(), field);
        }
    }

    public static List<Field> getDeclaredFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<Field>();
        getDeclaredFields(clazz, fieldList);
        return fieldList;
    }

    private static void getDeclaredFields(Class<?> clazz, List<Field> fieldList) {
        if (clazz == null) {
            return;
        }
        if (clazz == Object.class) {
            return;
        }
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        getDeclaredFields(clazz.getSuperclass(), fieldList);
    }
}
