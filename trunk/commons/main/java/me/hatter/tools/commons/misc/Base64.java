package me.hatter.tools.commons.misc;

import java.lang.reflect.Method;

import me.hatter.tools.commons.io.IOUtil;

public class Base64 {

    private static String   CLS = "java.util.prefs.Base64";
    private static Class<?> CLAZZ;
    private static Method   m_byteArrayToBase64;
    private static Method   m_base64ToByteArray;
    static {
        try {
            CLAZZ = Class.forName(CLS);
            m_byteArrayToBase64 = CLAZZ.getDeclaredMethod("byteArrayToBase64", new Class<?>[] { byte[].class });
            m_base64ToByteArray = CLAZZ.getDeclaredMethod("base64ToByteArray", new Class<?>[] { String.class });
            m_byteArrayToBase64.setAccessible(true);
            m_base64ToByteArray.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    // do base64
    public static String atob(String a) {
        try {
            return new String(base64ToByteArray(a), IOUtil.CHARSET_UTF8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String atobx2(String a) {
        return atob(atob(a));
    }

    // un base64
    public static String btoa(String b) {
        try {
            return byteArrayToBase64(b.getBytes(IOUtil.CHARSET_UTF8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String btoax2(String b) {
        return btoa(btoa(b));
    }

    public static String byteArrayToBase64(byte[] a) {
        try {
            return (String) m_byteArrayToBase64.invoke(null, new Object[] { a });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] base64ToByteArray(String s) {
        try {
            return (byte[]) m_base64ToByteArray.invoke(null, new Object[] { s });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
