package me.hatter.tools.commons.object;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.reflect.ReflectUtil;
import me.hatter.tools.commons.string.StringUtil;

public class ObjectDisplayTool {

    public static interface ObjectWalker {

        boolean isSimple(Class<?> clazz);

        boolean accept(Class<?> clazz, Field field);

        void display(Field field, Object value, int depth, boolean duplicate, Writer writer) throws IOException;
    }

    public static class DefaultObjectWalker implements ObjectWalker {

        @Override
        public boolean isSimple(Class<?> clazz) {
            return ObjectDisplayTool.isSimpleClass(clazz);
        }

        @Override
        public boolean accept(Class<?> clazz, Field field) {
            if (Modifier.isStatic(field.getModifiers())) {
                return false; // ignore static
            }
            return true;
        }

        @Override
        public void display(Field field, Object value, int depth, boolean duplicate, Writer writer) throws IOException {
            if (depth > 100) {
                throw new RuntimeException("Depth is to large: " + depth);
            }
            String objAddr = null;
            if (value != null) {
                objAddr = value.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(value));
            }
            String padding = StringUtil.repeat("    ", depth);
            if (duplicate) {
                writer.write(padding + field.getName() + ": $REF(" + objAddr + ")" + Environment.LINE_SEPARATOR);
            } else {
                String _ov;
                if (value != null && value.getClass().isArray()) {
                    _ov = arrayToString(value);
                } else {
                    if (value == null) {
                        _ov = "$NULL";
                    } else {
                        String _sv = value.toString();
                        if (objAddr.equals(_sv)) {
                            _ov = _sv;
                        } else {
                            _ov = _sv + " (" + objAddr + ")";
                        }
                    }
                }
                if (field != null) {
                    writer.write(padding + field.getName() + ": " + _ov + Environment.LINE_SEPARATOR);
                } else {
                    writer.write(padding + _ov + Environment.LINE_SEPARATOR);
                }
            }
        }
    }

    private Object                           root;
    private Writer                           writer;
    private IdentityHashMap<Object, Integer> idMap = new IdentityHashMap<Object, Integer>();

    public ObjectDisplayTool(Object root) {
        this(root, null);
    }

    public ObjectDisplayTool(Object root, Writer writer) {
        this.root = root;
        if (writer == null) {
            this.writer = new Writer() {

                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                    System.out.print(new String(cbuf, off, len));
                }

                @Override
                public void flush() throws IOException {
                    System.out.flush();
                }

                @Override
                public void close() throws IOException {
                    this.flush(); // JUST FLUSH
                }
            };
        } else {
            this.writer = writer;
        }
    }

    public void display() {
        display(new DefaultObjectWalker());
    }

    public void display(ObjectWalker walker) {
        try {
            idMap.put(root, 1);
            walker.display(null, root, 0, false, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        _display(walker, root, new AtomicInteger(), writer);
    }

    public void _display(ObjectWalker walker, Object o, AtomicInteger depth, Writer writer) {
        depth.incrementAndGet();
        try {
            List<Field> fs = ReflectUtil.getDeclaredFields(o.getClass());
            for (Field f : fs) {
                if (walker.accept(o.getClass(), f)) {
                    f.setAccessible(true);
                    Object _o = f.get(o);
                    if (idMap.containsKey(_o)) {
                        walker.display(f, _o, depth.get(), true, writer);
                    } else {
                        if (_o != null) {
                            idMap.put(_o, 1);
                        }
                        walker.display(f, _o, depth.get(), false, writer);
                        if ((_o != null) && (!walker.isSimple(_o.getClass()))) {
                            _display(walker, _o, depth, writer);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            depth.decrementAndGet();
        }
    }

    public static String arrayToString(Object o) {
        if (o.getClass() == int[].class) {
            return Arrays.toString((int[]) o);
        } else if (o.getClass() == long[].class) {
            return Arrays.toString((long[]) o);
        } else if (o.getClass() == boolean[].class) {
            return Arrays.toString((boolean[]) o);
        } else if (o.getClass() == float[].class) {
            return Arrays.toString((float[]) o);
        } else if (o.getClass() == double[].class) {
            return Arrays.toString((double[]) o);
        } else if (o.getClass() == byte[].class) {
            return Arrays.toString((byte[]) o);
        } else if (o.getClass() == char[].class) {
            return Arrays.toString((char[]) o);
        } else {
            return Arrays.toString((Object[]) o);
        }
    }

    public static boolean isSimpleClass(Class<?> clazz) {
        if (clazz.isArray() || clazz.isPrimitive() || clazz.isEnum()) {
            return true;
        }
        if (Number.class.isAssignableFrom(clazz) || CharSequence.class.isAssignableFrom(clazz)
            || Character.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)) {
            return true;
        }

        return false;
    }
}
