package me.hatter.tools.commons.args;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UnixArgsutil {

    public static final UnixArgs ARGS = new UnixArgs();

    public static class UnixArgs {

        private List<String>              args      = new ArrayList<String>();
        private Set<String>               flags     = new LinkedHashSet<String>();
        private Map<String, List<String>> keyvalues = new LinkedHashMap<String, List<String>>();

        public String[] args() {
            return args.toArray(new String[0]);
        }

        public Set<String> flags() {
            return Collections.unmodifiableSet(flags);
        }

        public Set<String> keys() {
            return Collections.unmodifiableSet(keyvalues.keySet());
        }

        public String kvalue(String key) {
            List<String> vs = keyvalues.get(key);
            return ((vs == null) || (vs.size() == 0)) ? null : vs.get(0);
        }

        public List<String> kvalues(String key) {
            List<String> vs = keyvalues.get(key);
            return (vs == null) ? null : Collections.unmodifiableList(vs);
        }
    }

    public static void parseGlobalArgs(String[] args) {
        processArgs(ARGS, args);
    }

    public static UnixArgs parseArgs(String[] args) {
        UnixArgs unixArgs = new UnixArgs();
        processArgs(unixArgs, args);
        return unixArgs;
    }

    public static void processArgs(UnixArgs unixArgs, String[] args) {
        if (args != null) {
            String lastkv = null;
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (lastkv != null) {
                    List<String> vs = unixArgs.keyvalues.get(lastkv);
                    if (vs == null) {
                        vs = new ArrayList<String>();
                        unixArgs.keyvalues.put(lastkv, vs);
                    }
                    vs.add(arg);
                    lastkv = null;
                } else {
                    if (arg.startsWith("--")) {
                        unixArgs.flags.add(arg.substring(2));
                    } else if (arg.startsWith("-")) {
                        lastkv = arg.substring(1);
                    } else {
                        unixArgs.args.add(arg);
                    }
                }
            }
        }
    }
}
