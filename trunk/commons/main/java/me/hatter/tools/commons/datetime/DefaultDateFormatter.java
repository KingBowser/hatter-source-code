package me.hatter.tools.commons.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DefaultDateFormatter implements DateFormater {

    private String   pattern;
    private TimeZone timeZone;

    public DefaultDateFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public DateFormater timeZone(String tz) {
        return timeZone(TimeZone.getTimeZone(tz));
    }

    @Override
    public DateFormater timeZone(TimeZone tz) {
        DefaultDateFormatter df = new DefaultDateFormatter(this.pattern);
        df.timeZone = tz;
        return df;
    }

    @Override
    public String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (timeZone != null) {
            sdf.setTimeZone(timeZone);
        }
        return sdf.format(date);
    }

    @Override
    public Date parse(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (timeZone != null) {
            sdf.setTimeZone(timeZone);
        }
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}
