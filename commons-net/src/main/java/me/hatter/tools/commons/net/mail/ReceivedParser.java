package me.hatter.tools.commons.net.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.hatter.tools.commons.net.mail.header.Received;
import me.hatter.tools.commons.net.mail.header.ReceivedFrom;
import me.hatter.tools.commons.string.StringUtil;

public class ReceivedParser {

    private static Set<String> keys = new HashSet<String>(Arrays.asList("from", "for", "by", "with", "id", "over"));

    public static Received parseReceived(String received) {
        Map<String, String> receivedMap = _parseReceived(received);
        Received r = new Received();
        r.setFrom(parseReceivedFrom(receivedMap.get("from")));
        r.setBy(receivedMap.get("by"));
        r.setSfor(receivedMap.get("for"));
        r.setOver(receivedMap.get("over"));
        r.setWith(receivedMap.get("with"));
        r.setId(receivedMap.get("id"));
        r.setTime(parseDate(receivedMap.get("_time")));
        return r;
    }

    public static ReceivedFrom parseReceivedFrom(String from) {
        if (from == null) {
            return null;
        }
        String host = StringUtil.trim(StringUtil.substringBeforeLast(from, "("));
        String reservedHostAndIp = StringUtil.substringAfterLast(StringUtil.substringBeforeLast(from, ")"), "(");
        String message = StringUtil.trim(StringUtil.substringAfterLast(from, ")"));
        String reservedHost = StringUtil.trim(StringUtil.substringBeforeLast(reservedHostAndIp, "["));
        String ip = StringUtil.trim(StringUtil.substringAfterLast(StringUtil.substringBeforeLast(reservedHostAndIp, "]"),
                                                                  "["));
        if (!reservedHostAndIp.contains("[")) {
            // from hostname(IP)
            ip = reservedHostAndIp;
        }

        ReceivedFrom f = new ReceivedFrom();
        f.setHost(host);
        f.setReservedHost(reservedHost);
        f.setIp(ip);
        f.setMessage(message);
        return f;
    }

    private static Map<String, String> _parseReceived(String received) {
        received = received.replaceAll("\\s+", " ");
        Map<String, String> receivedMap = new LinkedHashMap<String, String>();
        String time = StringUtil.substringAfterLast(received, ";").trim();
        received = StringUtil.substringBeforeLast(received, ";").trim();
        String lastr = null;
        List<String> t = new ArrayList<String>();
        String[] rs = split(received.trim());
        for (String r : rs) {
            // System.out.println(r);
            if (keys.contains(StringUtil.toLowerCase(r))) {
                if (lastr != null) {
                    receivedMap.put(lastr, StringUtil.trim(StringUtil.join(t, " ")));
                }
                lastr = StringUtil.toLowerCase(r);
                t.clear();
            } else {
                t.add(r);
            }
            if (lastr != null) {
                receivedMap.put(lastr, StringUtil.trim(StringUtil.join(t, " ")));
            }
        }
        receivedMap.put("_time", time);
        return receivedMap;
    }

    private static Date parseDate(String date) {
        if (date == null) {
            return null;
        }
        if (date.contains("(")) {
            date = StringUtil.substringBefore(date, "(");
        }
        return DateParser.parseDate(date);
    }

    private static String[] split(String s) {
        List<String> list = new ArrayList<String>();
        int inB = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c == '(') || (c == '[') || (c == '<')) {
                sb.append(c);
                inB++;
            } else if ((c == ')') || (c == ']') || (c == '>')) {
                sb.append(c);
                inB--;
            } else {
                if (inB > 0) {
                    sb.append(c);
                } else if ((c == ' ') || (c == '\t') || (c == '\r') || (c == '\n')) {
                    list.add(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    sb.append(c);
                }
            }
        }
        if (sb.length() > 0) {
            list.add(sb.toString());
        }
        return list.toArray(new String[0]);
    }
}
