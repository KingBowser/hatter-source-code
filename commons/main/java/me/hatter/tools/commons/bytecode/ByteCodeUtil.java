package me.hatter.tools.commons.bytecode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.function.Function;
import me.hatter.tools.commons.string.StringUtil;

public class ByteCodeUtil {

    // javax/swing/plaf/IconUIResource.paintIcon(Ljava/awt/Component;Ljava/awt/Graphics;II)V
    public static SimpleClassMethod parseClassMethod(String classMehtod) {
        int indexOfD = classMehtod.indexOf('.');
        int indexOfLK = classMehtod.indexOf('(');
        int indexOfRK = classMehtod.indexOf(')');

        String c = classMehtod.substring(0, indexOfD);
        String m = classMehtod.substring(indexOfD + 1, indexOfLK);
        String p = classMehtod.substring(indexOfLK + 1, indexOfRK);
        String r = classMehtod.substring(indexOfRK + 1);

        return new SimpleClassMethod(resolveClassName(r), resolveClassName(c), m, splitAndResolveParameters(p));
    }

    public static List<String> splitAndResolveParameters(String p) {
        return resolveClassNames(splitParameters(p));
    }

    public static List<String> splitParameters(String p) {
        List<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c == '[') {
                sb.append(c);
                i++;
                c = p.charAt(i);
            }
            if (c == 'L') {
                sb.append(c);
                LLOOP: for (i++; i < p.length(); i++) {
                    char c2 = p.charAt(i);
                    sb.append(c2);
                    if (c2 == ';') {
                        break LLOOP;
                    }
                }
            } else {
                sb.append(c);
            }
            list.add(sb.toString());
            sb.delete(0, sb.length());
        }
        if (sb.length() > 0) {
            list.add(sb.toString());
        }
        return list;
    }

    public static List<String> resolveClassNames(List<String> list) {
        return CollectionUtil.it(list).map(new Function<String, String>() {

            @Override
            public String apply(String obj) {
                return resolveClassName(obj);
            }
        }).asList();
    }

    public static String resolveClassName(String className) {
        return resolveClassName(className, false, new AtomicBoolean());
    }

    public static String resolveClassName(String className, boolean isShort, AtomicBoolean isPrimary) {
        if (className == null) {
            return null;
        }
        if (isPrimary != null) {
            isPrimary.set(false);
        }
        className = className.replace('/', '.');
        int arr = 0;
        while (className.startsWith("[")) {
            arr++;
            className = className.substring(1);
        }
        if (className.endsWith(";") && className.startsWith("L")) {
            className = className.substring(1, className.length() - 1);
        } else if (className.length() == 1) {
            String pn = getPrimaryTypeName(className.charAt(0));
            if ((pn != null) && (isPrimary != null)) {
                isPrimary.set(true);
            }
            className = (pn == null) ? className : pn;
        }
        if (arr > 0) {
            className = className + StringUtil.repeat("[]", arr);
        }

        if (!isShort) {
            return className;
        }
        return getSimpleClassName(className);
    }

    public static String getSimpleClassName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        return (lastIndexOfDot < 0) ? className : className.substring(lastIndexOfDot + 1);
    }

    public static String getPrimaryTypeName(char c) {
        String className = null;
        switch (c) {
            case 'V':
                className = "void";
                break;
            case 'Z':
                className = "boolean";
                break;
            case 'C':
                className = "char";
                break;
            case 'B':
                className = "byte";
                break;
            case 'S':
                className = "short";
                break;
            case 'I':
                className = "int";
                break;
            case 'F':
                className = "float";
                break;
            case 'J':
                className = "long";
                break;
            case 'D':
                className = "double";
                break;
        }
        return className;
    }
}
