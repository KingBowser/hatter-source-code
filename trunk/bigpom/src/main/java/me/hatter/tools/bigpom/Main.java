package me.hatter.tools.bigpom;

import me.hatter.tools.commons.args.UnixArgsutil;

public class Main {

    public static void main(String[] args) throws Exception {
        UnixArgsutil.parseGlobalArgs(args);

        if (UnixArgsutil.ARGS.flags().contains("checksvn")) {
            SvnCheck.main(args);
        }
        BigPom.main(args);
    }
}
