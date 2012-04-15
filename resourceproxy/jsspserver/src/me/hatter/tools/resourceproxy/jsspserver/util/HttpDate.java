package me.hatter.tools.resourceproxy.jsspserver.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HttpDate {

    // Sun, 06 Nov 1994 08:49:37 GMT ; RFC 822, updated by RFC 1123
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }
}
