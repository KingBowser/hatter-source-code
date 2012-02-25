package me.hatter.tools.resourceproxy.dbutils.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StringUtil {

    public static String EMPTY = "";

    public static String join(List<String> list, String separater) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            sb.append(separater);
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    public static String toUnder(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.charAt(0));
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
            }
            sb.append(c);
        }
        return sb.toString().toLowerCase();
    }

    public static String toCamel(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return EMPTY;
        }
        str = str.toLowerCase();
        StringBuilder sb = new StringBuilder();
        char firstChar = str.charAt(0);
        boolean lastIsUnder = false;
        sb.append(firstChar);
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                lastIsUnder = true;
                continue;
            }
            if (lastIsUnder) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(Character.toLowerCase(c));
            }
            lastIsUnder = false;
        }
        return sb.toString();
    }

    public static byte[] stringToByteArray(String string) {
        if ((string.length() % 2) > 0) {
            throw new RuntimeException("String input farmat error.");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < string.length(); i += 2) {
            char c1 = string.charAt(i);
            char c2 = string.charAt(i + 1);
            baos.write((int) (byte) Integer.parseInt(new String(new char[] { c1, c2 }), 16));
        }
        return baos.toByteArray();
    }

    public static String byteArrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString((int) bytes[i]);
            if (hex.length() == 0) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String headerToString(Map<String, List<String>> headerMap) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        for (String key : headerMap.keySet()) {
            List<String> valueList = headerMap.get(key);
            for (String value : valueList) {
                pw.println(key + ":" + value);
            }
        }
        return sw.toString();
    }

    public static Map<String, List<String>> stringToHeader(String str) {
        StringReader sr = new StringReader(str.trim());
        BufferedReader br = new BufferedReader(sr);
        Map<String, List<String>> headerMap = new LinkedHashMap<String, List<String>>();
        try {
            for (String line; ((line = br.readLine()) != null);) {
                int indexOfComma = line.indexOf(':');
                String key = line.substring(0, indexOfComma);
                String value = line.substring(indexOfComma + 1);
                List<String> valueList = headerMap.get(key);
                if (valueList == null) {
                    valueList = new ArrayList<String>();
                    headerMap.put(key, valueList);
                }
                valueList.add(value);
            }
            return headerMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
