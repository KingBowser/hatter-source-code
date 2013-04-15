package me.hatter.tools.commons.lang;

public class ReferenceObject<T> {

    private T object;

    public ReferenceObject() {
        this(null);
    }

    public ReferenceObject(T object) {
        this.object = object;
    }

    public T getValue() {
        return object;
    }

    public void setValue(T object) {
        this.object = object;
    }
}
