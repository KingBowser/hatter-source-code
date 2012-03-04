package me.hatter.tools.resourceproxy.commons.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KeyValueListMap extends LinkedHashMap<String, List<String>> {

    private static final long serialVersionUID = 1L;

    public KeyValueListMap() {
    }

    public KeyValueListMap(KeyValueListMap keyValueListMap) {
        super(keyValueListMap);
    }

    public void addMap(KeyValueListMap keyValueListMap) {
        for (Map.Entry<String, List<String>> entry : keyValueListMap.entrySet()) {
            addList(entry.getKey(), entry.getValue());
        }
    }

    public void set(String key, String value) {
        List<String> list = new ArrayList<String>(1);
        list.add(value);
        put(key, list);
    }

    public void add(String key, String value) {
        List<String> valueList = get(key);
        if (valueList == null) {
            valueList = new ArrayList<String>();
            put(key, valueList);
        }
        valueList.add(value);
    }

    public void addList(String key, List<String> vList) {
        if (vList == null) {
            return;
        }
        List<String> valueList = get(key);
        if (valueList == null) {
            valueList = new ArrayList<String>();
            put(key, valueList);
        }
        valueList.addAll(vList);
    }

    public String getFirst(String key) {
        List<String> valueList = get(key);
        return ((valueList == null) || (valueList.isEmpty())) ? null : valueList.get(0);
    }
}
