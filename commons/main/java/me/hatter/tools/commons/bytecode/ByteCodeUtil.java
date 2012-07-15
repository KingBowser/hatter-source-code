package me.hatter.tools.commons.bytecode;

import me.hatter.tools.commons.string.StringUtil;

public class ByteCodeUtil {

    public static String resolveClassName(String className, boolean isShort) {
        if (className == null) {
            return null;
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
            className = (pn == null) ? className : pn;
        }
        if (arr > 0) {
            className = className + StringUtil.repeat("[]", arr);
        }

        if (!isShort) {
            return className;
        }
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
