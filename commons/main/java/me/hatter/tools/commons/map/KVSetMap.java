package me.hatter.tools.commons.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KVSetMap<K, V> extends HashMap<K, Set<V>> {

    private static final long serialVersionUID = 1970502317883603516L;

    public void add(K k, V v) {
        Set<V> vSet = get(k);
        if (vSet == null) {
            vSet = new HashSet<V>();
            put(k, vSet);
        }
        vSet.add(v);
    }
}
