package me.hatter.tools.commons.lang;

public class ReferenceInt {

    private int value;

    public ReferenceInt() {
        this(0);
    }

    public ReferenceInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
