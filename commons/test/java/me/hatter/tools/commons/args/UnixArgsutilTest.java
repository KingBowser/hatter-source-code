package me.hatter.tools.commons.args;

import java.util.Arrays;

import me.hatter.tools.commons.args.UnixArgsUtil.UnixArgs;

public class UnixArgsutilTest {

    public static void main(String[] args) {
        {
            UnixArgs ua = UnixArgsUtil.parseArgs(new String[] { "--user-name=hatter", "--test", "-flag", "f", "a" });
            System.out.println(Arrays.asList(ua.args()));
            System.out.println(ua.flags());
            for (String k : ua.keys()) {
                System.out.println(k);
                System.out.println("    " + ua.kvalues(k));
            }
        }
        {
            UnixArgs ua = UnixArgsUtil.parseArgs(new String[] { "-p", "8080" });
            System.out.println(Arrays.asList(ua.args()));
            System.out.println(ua.flags());
            for (String k : ua.keys()) {
                System.out.println(k);
                System.out.println("    " + ua.kvalues(k));
            }
        }
    }
}
