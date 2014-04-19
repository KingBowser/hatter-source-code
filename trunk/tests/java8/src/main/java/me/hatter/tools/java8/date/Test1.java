package me.hatter.tools.java8.date;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * Created by hatterjiang on 4/4/14.
 */
public class Test1 {
    public static void main(String[] args) {
        System.out.println(LocalDate.of(2013, Month.APRIL, 1));
        System.out.println(LocalDate.now());
        System.out.println(ZonedDateTime.now());
        System.out.println(ZonedDateTime.now(ZoneId.of("UTC")));
        System.out.println(ZonedDateTime.now(ZoneId.of("America/Los_Angeles")));
        System.out.println(TimeZone.getTimeZone("PST"));
        System.out.println(ZonedDateTime.now(TimeZone.getTimeZone("PST").toZoneId()));
    }
}
