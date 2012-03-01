package me.hatter.tools.resourceproxy.commons.util;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {

    private static final long serialVersionUID = -4802338129507268836L;

    private K                 k;
    private V                 v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }

    @Override
    public int hashCode() {
        int kc = (k == null) ? 0 : k.hashCode();
        int vc = (v == null) ? 0 : v.hashCode();
        return ((kc * 32) + vc);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object another) {
        if ((another == null) || (!(another instanceof Pair))) {
            return false;
        }
        Pair<K, V> anotherPair = (Pair<K, V>) another;
        return (objectEquals(k, anotherPair.k) && objectEquals(v, anotherPair.v));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[key=");
        sb.append((k == null) ? "null" : k.toString());
        sb.append(", ");
        sb.append("value=");
        sb.append((v == null) ? "null" : v.toString());
        sb.append("]");
        return sb.toString();
    }

    private static boolean objectEquals(Object obj0, Object obj1) {
        if (obj0 == obj1) {
            return true;
        }
        if (obj0 == null) {
            return false;
        }
        return obj0.equals(obj1);
    }
}
