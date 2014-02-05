package me.hatter.tools.commons.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// TODO
public class AdvancedArgsUtil {

    private static KArgs                    kargs         = null;
    private static Set<String>              flagArgsSet   = new HashSet<String>();
    private static Set<String>              kvalueArgsSet = new HashSet<String>();
    private static Map<String, Set<String>> aliasMap      = new HashMap<String, Set<String>>();

    public static void addFlagArgsSet(String... keys) {
        flagArgsSet.addAll(Arrays.asList(keys));
    }

    public static void addKvalueArgsSet(String... keys) {
        kvalueArgsSet.addAll(Arrays.asList(keys));
    }

    public static void parse(String[] args) {
        if (kargs != null) {
            throw new IllegalStateException("Arguments is already parsed!");
        }
        // TODO
    }

    public static KArgs args() {
        if (kargs == null) {
            throw new IllegalStateException("Arguments is not yet parsed!");
        }
        return kargs;
    }
}
