package me.hatter.tools.commons.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.hatter.tools.commons.shortutil.Cu;

public class DateTimeUtil {

    public static final String   PATTERN_1     = "d MMM yyyy HH:mm:ss Z";
    public static final String   PATTERN_NGINX = "dd/MMM/yyyy:HH:mm:ss Z";

    volatile private static long adjustMillis  = 0L;

    public static long currentMillis() {
        return System.currentTimeMillis() + adjustMillis;
    }

    // --------------------------------------------------

    public static long currMillis() {
        return currentMillis();
    }

    public static Calendar calendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        return cal;
    }

    public static Calendar calendar() {
        return calendar(date());
    }

    public static Date date() {
        return new Date(currentMillis());
    }

    public static Date after(long millis) {
        return new Date(currentMillis() + millis);
    }

    public static Date before(long millis) {
        return new Date(currentMillis() - millis);
    }

    public static Date after(long time, TimeUnit unit) {
        return new Date(currentMillis() + unit.toMillis(time));
    }

    public static Date before(long time, TimeUnit unit) {
        return new Date(currentMillis() - unit.toMillis(time));
    }

    public static Date after(Date date, long time, TimeUnit unit) {
        return new Date(date.getTime() + unit.toMillis(time));
    }

    public static Date before(Date date, long time, TimeUnit unit) {
        return new Date(date.getTime() - unit.toMillis(time));
    }

    public static Date parse(String pattern, String date) {
        return parse(new SimpleDateFormat(pattern), date);
    }

    public static String format(String pattern, Date date) {
        return format(new SimpleDateFormat(pattern), date);
    }

    public static Date parse(SimpleDateFormat sdf, String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String format(SimpleDateFormat sdf, Date date) {
        return sdf.format(date);
    }

    public static String format(Times times) {
        long _c;
        long _m = times.toMillis();
        List<String> result = new ArrayList<String>();

        if (_m < 1000) {
            return _m + "ms";
        }
        _m /= 1000; // s
        _c = _m % 60;
        _m /= 60; // m
        if (_c > 0 && _m < 60) {
            result.add(_c + "s");
        }
        if (_m > 0) {
            _c = _m % 60;
            _m /= 60; // h
            if (_c > 0) {
                result.add(_c + "m");
            }
            if (_m > 0) {
                _c = _m % 24;
                _m /= 24; // d
                if (_c > 0) {
                    result.add(_c + "h");
                }
                if (_m > 0) {
                    result.add(_m + "d");
                }
            }
        }

        return Cu.it(result).reverse().join(" ");
    }

    public static String formatSingle(Times times) {
        long millis = times.toMillis();
        long _m = millis;
        if (_m < 1000) {
            return _m + " ms";
        }
        _m /= 1000; // s
        if (_m < 60) {
            return _m + " s";
        }
        _m /= 60; // m
        if (_m < 60) {
            return _m + " m";
        }
        _m /= 60; // h
        if (_m < 24) {
            return _m + " h";
        }
        _m /= 24; // d
        return _m + " d";
    }
}
