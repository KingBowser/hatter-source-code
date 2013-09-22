package me.hatter.tools.commons.datetime;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {

    volatile private static long adjustMillis = 0L;

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
}
