package me.hatter.tools.commons.lang;

public class ReferenceLong {

    private long value;

    public ReferenceLong() {
        this(0L);
    }

    public ReferenceLong(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
