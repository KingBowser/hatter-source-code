package me.hatter.tools.commons.net.mail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateParser {

    public static Date parseDate(String d) {
        List<SimpleDateFormat> dfs = Arrays.asList(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"),
                                                   new SimpleDateFormat("d MMM yyyy HH:mm:ss Z"));
        Exception ex = null;
        for (SimpleDateFormat df : dfs) {
            try {
                return df.parse(d);
            } catch (ParseException e) {
                ex = e;
            }
        }
        throw new RuntimeException(ex);
    }
}
