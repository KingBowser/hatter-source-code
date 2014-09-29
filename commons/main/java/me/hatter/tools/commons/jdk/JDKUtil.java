package me.hatter.tools.commons.jdk;

import java.util.HashMap;
import java.util.Map;

public class JDKUtil {

    public static enum JDKVersion {
        JDK1_0, JDK1_1, JDK1_2, JDK1_3, JDK1_4,

        JDK1_5, JDK1_6, JDK1_7, JDK1_8, JDK1_9,

        Unknow
    }

    private static Map<JDKVersion, Integer> jdkVersionMap   = new HashMap<JDKUtil.JDKVersion, Integer>();
    static {
        jdkVersionMap.put(JDKVersion.JDK1_0, Integer.valueOf(10));
        jdkVersionMap.put(JDKVersion.JDK1_1, Integer.valueOf(11));
        jdkVersionMap.put(JDKVersion.JDK1_2, Integer.valueOf(12));
        jdkVersionMap.put(JDKVersion.JDK1_3, Integer.valueOf(13));
        jdkVersionMap.put(JDKVersion.JDK1_4, Integer.valueOf(14));
        jdkVersionMap.put(JDKVersion.JDK1_5, Integer.valueOf(15));
        jdkVersionMap.put(JDKVersion.JDK1_6, Integer.valueOf(16));
        jdkVersionMap.put(JDKVersion.JDK1_7, Integer.valueOf(17));
        jdkVersionMap.put(JDKVersion.JDK1_8, Integer.valueOf(18));
        jdkVersionMap.put(JDKVersion.JDK1_9, Integer.valueOf(19));
    }

    private static final String             javaSpecVersion = System.getProperty("java.specification.version");

    public static boolean isOrAbove(JDKVersion version) {
        Integer internalIndex = jdkVersionMap.get(version);
        if (internalIndex == null) {
            return false;
        }
        Integer currentInternalIndex = jdkVersionMap.get(getJDKVersion());
        if (currentInternalIndex == null) {
            return false;
        }
        return (internalIndex.intValue() >= currentInternalIndex.intValue());
    }

    public static JDKVersion getJDKVersion() {
        if ("1.0".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_0;
        } else if ("1.1".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_1;
        } else if ("1.2".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_2;
        } else if ("1.3".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_3;
        } else if ("1.4".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_4;
        } else if ("1.5".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_5;
        } else if ("1.6".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_6;
        } else if ("1.7".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_7;
        } else if ("1.8".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_8;
        } else if ("1.9".equals(javaSpecVersion)) {
            return JDKVersion.JDK1_9;
        }
        return JDKVersion.Unknow;
    }
}
