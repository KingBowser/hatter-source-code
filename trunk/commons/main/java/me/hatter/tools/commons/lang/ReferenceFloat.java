package me.hatter.tools.commons.lang;

public class ReferenceFloat {

    private float value;

    public ReferenceFloat() {
        this(0.0F);
    }

    public ReferenceFloat(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
