package me.hatter.tools.commons.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KVListMap<K, V> extends HashMap<K, List<V>> {

    private static final long serialVersionUID = 1970502317883603516L;

    public void add(K k, V v) {
        List<V> vList = get(k);
        if (vList == null) {
            vList = new ArrayList<V>();
            put(k, vList);
        }
        vList.add(v);
    }
}
