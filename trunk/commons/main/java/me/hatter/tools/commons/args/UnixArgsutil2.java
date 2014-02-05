package me.hatter.tools.commons.args;

import java.util.ArrayList;
import java.util.List;

public class UnixArgsutil2 {

    public static final KArgs ARGS = new KArgs();

    public static PICArgs parseGlobalPICArgs(String[] args, long defInterval, int defcount) {
        parseGlobalArgs(args);
        PICArgs picArgs = new PICArgs(defInterval, defcount);
        picArgs.parseArgs(UnixArgsutil2.ARGS.args());
        return picArgs;
    }

    public static PICArgs parseGlobalPICArgs(String[] args) {
        parseGlobalArgs(args);
        PICArgs picArgs = new PICArgs();
        picArgs.parseArgs(UnixArgsutil2.ARGS.args());
        return picArgs;
    }

    public static void parseGlobalArgs(String[] args) {
        processArgs(ARGS, args);
    }

    public static KArgs parseArgs(String[] args) {
        KArgs unixArgs = new KArgs();
        processArgs(unixArgs, args);
        return unixArgs;
    }

    public static void processArgs(KArgs unixArgs, String[] args) {
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
