package me.hatter.tools.resourceproxy.commons.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.hatter.tools.commons.collection.CollectionUtil;

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
        return CollectionUtil.firstObject(get(key));
    }

    public List<String> getIgnoreCase(String key) {
        if (key == null) {
            return this.get(key);
        }
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, List<String>> entry : this.entrySet()) {
            if (key.equalsIgnoreCase(entry.getKey())) {
                list.addAll(entry.getValue());
            }
        }
        return list;
    }

    public String getFirstIgnoreCase(String key) {
        return CollectionUtil.firstObject(getIgnoreCase(key));
    }
}
