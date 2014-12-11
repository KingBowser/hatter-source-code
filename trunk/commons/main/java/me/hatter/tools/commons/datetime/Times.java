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

    public Times add(Times times) {
        TimeUnit timeUnit = this.unit;
        TimeUnit anotherTimeUnit = times.unit;

        TimeUnit lowTu = lowTimeUnit(timeUnit, anotherTimeUnit);

        if (lowTu == TimeUnit.NANOSECONDS) {
            return NANOSECONDS(this.toNanos() + times.toNanos());
        }
        if (lowTu == TimeUnit.MICROSECONDS) {
            return MICROSECONDS(this.toMicros() + times.toMicros());
        }
        if (lowTu == TimeUnit.MILLISECONDS) {
            return MILLISECONDS(this.toMillis() + times.toMillis());
        }
        if (lowTu == TimeUnit.SECONDS) {
            return SECONDS(this.toSeconds() + times.toSeconds());
        }
        if (lowTu == TimeUnit.MINUTES) {
            return MINUTES(this.toMinutes() + times.toMinutes());
        }
        if (lowTu == TimeUnit.HOURS) {
            return HOURS(this.toHours() + times.toHours());
        }
        if (lowTu == TimeUnit.DAYS) {
            return DAYS(this.toDays() + times.toDays());
        }
        throw new RuntimeException("Unknow time unit: " + lowTu);
    }

    public Times minus(Times times) {
        TimeUnit timeUnit = this.unit;
        TimeUnit anotherTimeUnit = times.unit;

        TimeUnit lowTu = lowTimeUnit(timeUnit, anotherTimeUnit);

        if (lowTu == TimeUnit.NANOSECONDS) {
            return NANOSECONDS(this.toNanos() - times.toNanos());
        }
        if (lowTu == TimeUnit.MICROSECONDS) {
            return MICROSECONDS(this.toMicros() - times.toMicros());
        }
        if (lowTu == TimeUnit.MILLISECONDS) {
            return MILLISECONDS(this.toMillis() - times.toMillis());
        }
        if (lowTu == TimeUnit.SECONDS) {
            return SECONDS(this.toSeconds() - times.toSeconds());
        }
        if (lowTu == TimeUnit.MINUTES) {
            return MINUTES(this.toMinutes() - times.toMinutes());
        }
        if (lowTu == TimeUnit.HOURS) {
            return HOURS(this.toHours() - times.toHours());
        }
        if (lowTu == TimeUnit.DAYS) {
            return DAYS(this.toDays() - times.toDays());
        }
        throw new RuntimeException("Unknow time unit: " + lowTu);
    }

    public static Times add(Times... times) {
        if ((times == null) || (times.length == 0)) {
            return Times.SECONDS(0);
        }
        Times t = times[0];
        for (int i = 1; i < times.length; i++) {
            t = t.add(times[i]);
        }
        return t;
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

    static TimeUnit lowTimeUnit(TimeUnit tu1, TimeUnit tu2) {
        if ((tu1 == TimeUnit.NANOSECONDS) || (tu2 == TimeUnit.NANOSECONDS)) {
            return TimeUnit.NANOSECONDS;
        }
        if ((tu1 == TimeUnit.MICROSECONDS) || (tu2 == TimeUnit.MICROSECONDS)) {
            return TimeUnit.MICROSECONDS;
        }
        if ((tu1 == TimeUnit.MILLISECONDS) || (tu2 == TimeUnit.MILLISECONDS)) {
            return TimeUnit.MILLISECONDS;
        }
        if ((tu1 == TimeUnit.SECONDS) || (tu2 == TimeUnit.SECONDS)) {
            return TimeUnit.SECONDS;
        }
        if ((tu1 == TimeUnit.MINUTES) || (tu2 == TimeUnit.MINUTES)) {
            return TimeUnit.MINUTES;
        }
        if ((tu1 == TimeUnit.HOURS) || (tu2 == TimeUnit.HOURS)) {
            return TimeUnit.HOURS;
        }
        if ((tu1 == TimeUnit.DAYS) || (tu2 == TimeUnit.DAYS)) {
            return TimeUnit.DAYS;
        }
        throw new RuntimeException("Unknow time unit: " + tu1 + " & " + tu2);
    }
}
