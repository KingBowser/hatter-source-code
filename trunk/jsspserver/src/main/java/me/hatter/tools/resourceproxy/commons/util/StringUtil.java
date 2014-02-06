package me.hatter.tools.resourceproxy.commons.util;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hatter.tools.commons.misc.Base64;

public class StringUtil {

    public static final StringUtil INSTANCE = new StringUtil();

    public static String           EMPTY    = "";

    public static String trim(String str) {
        return (str == null) ? null : str.trim();
    }

    public static String trimToNull(String str) {
        str = trim(str);
        return ((str == null) || str.isEmpty()) ? null : str;
    }

    public static String toUpperCase(String string) {
        return (string == null) ? null : string.toUpperCase();
    }

    public static String toLowerCase(String string) {
        return (string == null) ? null : string.toLowerCase();
    }

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
        if (string == null) {
            return null;
        }
        return Base64.base64ToByteArray(string);
    }

    public static String byteArrayToString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.byteArrayToBase64(bytes);
    }

    public static String headerToString(KeyValueListMap headerMap) {
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

    public static KeyValueListMap stringToHeader(String str) {
        StringReader sr = new StringReader(str.trim());
        BufferedReader br = new BufferedReader(sr);
        KeyValueListMap headerMap = new KeyValueListMap();
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

    public static final boolean isEmpty(final String str) {
        return ((str == null) || (str.length() == 0));
    }

    public static final boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }

    public static final String notNull(final String str) {
        return (str == null) ? "" : str;
    }

    public static final String upper(final String str) {
        return isEmpty(str) ? str : str.toUpperCase();
    }

    public static final String lower(final String str) {
        return isEmpty(str) ? str : str.toLowerCase();
    }

    public static final boolean equals(final String str0, final String str1) {
        if (str0 == str1) {
            return true;
        }
        return ((str0 != null) && str0.equals(str1));
    }

    public static final boolean contains(final String str0, final String str1) {
        if (str0 == null) {
            return (str1 == null);
        }
        return str0.contains(str1);
    }

    public static final List<Pair<String, String>> makePairs(final String s, final String regSep, final char eq) {
        if (isEmpty(s)) {
            return new ArrayList<Pair<String, String>>();
        }
        List<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
        String[] pairss = s.split(regSep);
        for (int i = 0; i < pairss.length; i++) {
            String key = pairss[i];
            String value = "";
            int equal = key.indexOf(eq);
            if (equal >= 0) {
                value = key.substring(equal + 1);
                key = key.substring(0, equal);
            }
            pairs.add(new Pair<String, String>(key, value));
        }
        return pairs;
    }

    public static final Map<String, String> pairsToMap(final List<Pair<String, String>> pairs) {
        Map<String, String> map = new HashMap<String, String>();
        if (pairs != null) {
            for (Pair<String, String> pair : pairs) {
                map.put(pair.getKey(), pair.getValue());
            }
        }
        return map;
    }

    public static String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        int len = html.length();
        StringBuilder sb = new StringBuilder(len + 100);
        for (int i = 0; i < len; i++) {
            char c = html.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static String preTextHtml(String html) {
        if (html == null) {
            return null;
        }
        int len = html.length();
        StringBuilder sb = new StringBuilder(len + 100);
        for (int i = 0; i < len; i++) {
            char c = html.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString().replace(" ", "&nbsp;").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;").replaceAll("((\r\n)|(\r)|(\n))",
                                                                                                         "<br/>");
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String formatDateMMDD(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("MM-dd").format(date);
    }

    public static String showDateColor(Date date) {
        if (date == null) {
            return "";
        }
        String GREY = "grey";
        String RED = "red";
        String PINK = "pink";
        String BLUE = "lightblue";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            date = sdf.parse(sdf.format(date));
            now = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (date.getTime() == now.getTime()) {
            return RED;
        }
        if (date.getTime() < now.getTime()) {
            return GREY;
        }
        if ((date.getTime() - now.getTime()) <= (1000 * 60 * 60 * 24)) {
            return PINK;
        }
        return BLUE;
    }

    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String printStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
