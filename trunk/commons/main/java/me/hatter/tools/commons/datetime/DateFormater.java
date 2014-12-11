package me.hatter.tools.commons.datetime;

import java.util.Date;
import java.util.TimeZone;

public interface DateFormater {

    DateFormater timeZone(String tz);

    DateFormater timeZone(TimeZone tz);

    String format(Date date);

    Date parse(String date);

    String getPattern();
}
