package me.hatter.tools.commons.lang;

public class ReferenceBool {

    private boolean value;

    public ReferenceBool() {
        this(false);
    }

    public ReferenceBool(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
