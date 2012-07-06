package me.hatter.tools.histoana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassCountSizeMap extends HashMap<String, ClassCountSize> {

    private static final long serialVersionUID = 1L;

    public static List<ClassCountSize> diff(ClassCountSizeMap m0, ClassCountSizeMap m1) {
        List<ClassCountSize> result = new ArrayList<ClassCountSize>();
        for (String n : m1.keySet()) {
            ClassCountSize c0 = m0.get(n);
            ClassCountSize c1 = m1.get(n);
            if (c0 == null) {
                result.add(c1);
            } else {
                if (c1.count > c0.count) {
                    ClassCountSize nc = new ClassCountSize();
                    nc.name = c1.name;
                    nc.count = c1.count - c0.count;
                    nc.size = c1.size - c0.size;
                    result.add(nc);
                }
            }
        }
        return result;
    }
}
