package me.hatter.tools.commons.lang;

public class ReferenceDouble {

    private double value;

    public ReferenceDouble() {
        this(0.0D);
    }

    public ReferenceDouble(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
