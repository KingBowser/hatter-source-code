package me.hatter.tools.commons.args;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UnixArgsutil {

    public static final UnixArgs ARGS = new UnixArgs();

    public static class UnixArgs {

        private KList       args      = new KList();
        private KSet        flags     = new KSet();
        private KMap        keyvalues = new KMap();
        private Set<String> fset      = new HashSet<String>();

        private String[]    _args     = null;
        private KSet        _keys     = null;

        public void addFSet(String... fkeys) {
            if ((fkeys != null) && (fkeys.length > 0)) {
                for (String fk : fkeys) {
                    fset.add(fk);
                }
            }
        }

        public String[] args() {
            synchronized (this) {
                if (_args == null) {
                    _args = args.toArray(new String[0]);
                }
            }
            return _args;
        }

        public KSet flags() {
            return flags;
        }

        public KSet keys() {
            synchronized (this) {
                if (_keys == null) {
                    _keys = new KSet(keyvalues.keySet());
                }
            }
            return _keys;
        }

        public String kvalue(String key) {
            List<String> vs = keyvalues.get(key);
            return ((vs == null) || (vs.size() == 0)) ? null : vs.get(0);
        }

        public String kvalueAny(String... keys) {
            if ((keys != null) && (keys.length > 0)) {
                for (String key : keys) {
                    List<String> vs = keyvalues.get(key);
                    return ((vs == null) || (vs.size() == 0)) ? null : vs.get(0);
                }
            }
            return null;
        }

        public String kvalue(String key, String defval) {
            List<String> vs = keyvalues.get(key);
            return ((vs == null) || (vs.size() == 0)) ? defval : vs.get(0);
        }

        public List<String> kvalues(String key) {
            List<String> vs = keyvalues.get(key);
            return (vs == null) ? null : Collections.unmodifiableList(vs);
        }

        public List<String> kvalues(String key, List<String> defval) {
            List<String> vs = keyvalues.get(key);
            return (vs == null) ? defval : Collections.unmodifiableList(vs);
        }
    }

    public static class KList extends ArrayList<String> {

        private static final long serialVersionUID = -156830291036307953L;
    }

    public static class KSet extends LinkedHashSet<String> {

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

    public static class KMap extends LinkedHashMap<String, List<String>> {

        private static final long serialVersionUID = -156830291036307953L;
    }

    public static PICArgs parseGlobalPICArgs(String[] args, long defInterval, int defcount) {
        parseGlobalArgs(args);
        PICArgs picArgs = new PICArgs(defInterval, defcount);
        picArgs.parseArgs(UnixArgsutil.ARGS.args());
        return picArgs;
    }

    public static PICArgs parseGlobalPICArgs(String[] args) {
        parseGlobalArgs(args);
        PICArgs picArgs = new PICArgs();
        picArgs.parseArgs(UnixArgsutil.ARGS.args());
        return picArgs;
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
            String lastk = null;
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (lastk != null) {
                    List<String> vs = unixArgs.keyvalues.get(lastk);
                    if (vs == null) {
                        vs = new ArrayList<String>();
                        unixArgs.keyvalues.put(lastk, vs);
                    }
                    vs.add(arg);
                    lastk = null;
                } else {
                    if (arg.startsWith("-J-D")) {
                        String ar = arg.substring(4);
                        int indexOfEq = ar.indexOf('=');
                        if (indexOfEq == 0) {
                            // IGNORE
                        } else if (indexOfEq < 0) {
                            System.setProperty(ar, null);
                        } else {
                            System.setProperty(ar.substring(0, indexOfEq), ar.substring(indexOfEq + 1));
                        }
                    } else if (arg.startsWith("---")) {
                        String ar = arg.substring(3);
                        for (char c : ar.toCharArray()) {
                            unixArgs.flags.add(new String(new char[] { c }));
                        }
                    } else if (arg.startsWith("--")) {
                        String ar = arg.substring(2);
                        int indexOfEq = ar.indexOf('=');
                        if (indexOfEq > 0) {
                            String k = ar.substring(0, indexOfEq);
                            String v = ar.substring(indexOfEq + 1);

                            List<String> vs = unixArgs.keyvalues.get(k);
                            if (vs == null) {
                                vs = new ArrayList<String>();
                                unixArgs.keyvalues.put(k, vs);
                            }
                            vs.add(v);
                        } else {
                            unixArgs.flags.add(ar);
                        }
                    } else if (arg.startsWith("-")) {
                        String kk = arg.substring(1);
                        if (unixArgs.fset.contains(kk)) {
                            unixArgs.flags.add(kk);
                        } else {
                            lastk = arg.substring(1);
                        }
                    } else {
                        unixArgs.args.add(arg);
                    }
                }
            }
        }
    }
}
