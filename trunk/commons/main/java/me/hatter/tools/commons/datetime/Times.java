package me.hatter.tools.commons.datetime;

import java.util.concurrent.TimeUnit;

public class Times {

    private long     time;
    private TimeUnit unit;

    protected Times(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    public long toNanos() {
        return unit.toNanos(time);
    }

    public long toMicros() {
        return unit.toMicros(time);
    }

    public long toMillis() {
        return unit.toMillis(time);
    }

    public long toSeconds() {
        return unit.toSeconds(time);
    }

    public long toMinutes() {
        return unit.toMinutes(time);
    }

    public long toHours() {
        return unit.toHours(time);
    }

    public long toDays() {
        return unit.toDays(time);
    }

    public static Times NANOSECONDS(long time) {
        return new Times(time, TimeUnit.NANOSECONDS);
    }

    public static Times MICROSECONDS(long time) {
        return new Times(time, TimeUnit.MICROSECONDS);
    }

    public static Times MILLISECONDS(long time) {
        return new Times(time, TimeUnit.MILLISECONDS);
    }

    public static Times SECONDS(long time) {
        return new Times(time, TimeUnit.SECONDS);
    }

    public static Times MINUTES(long time) {
        return new Times(time, TimeUnit.MINUTES);
    }

    public static Times HOURS(long time) {
        return new Times(time, TimeUnit.HOURS);
    }

    public static Times DAYS(long time) {
        return new Times(time, TimeUnit.DAYS);
    }
}
