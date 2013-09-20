package me.hatter.tools.resourceproxy.commons.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUtil {

    public static final JavaUtil INSTANCE = new JavaUtil();

    public static Map<?, ?> map() {
        return new HashMap<Object, Object>();
    }

    public static Map<?, ?> linkedMap() {
        return new HashMap<Object, Object>();
    }

    public static List<?> list() {
        return new ArrayList<Object>();
    }

    public static Set<?> set() {
        return new HashSet<Object>();
    }

    public static Set<?> linkedSet() {
        return new LinkedHashSet<Object>();
    }

    public static SimpleDateFormat dateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static Date date() {
        return new Date();
    }

    public static long millis() {
        return System.currentTimeMillis();
    }
}
