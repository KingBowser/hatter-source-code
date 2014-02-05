package me.hatter.tools.commons.args.util;

import java.util.Collection;
import java.util.LinkedHashSet;

public class KSet extends LinkedHashSet<String> {

    private static final long serialVersionUID = 178791470286450518L;

    public KSet() {
    }

    public KSet(Collection<? extends String> c) {
        super(c);
    }

    public boolean containsAny(String key, String... keys) {
        if (this.contains(key)) {
            return true;
        }
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                if (this.contains(keys[i])) {
                    return true;
                }
            }
        }
        return false;
    }
}
