package me.hatter.tools.cook.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Env {

    public static final String HOME_DIR = System.getProperty("home.dir");
    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String UTF_8    = "UTF-8";

    public static String[] parseArgs(String[] args) {
        List<String> argList = new ArrayList<String>();
        if (args != null) {
            for (String arg : args) {
                if (arg.startsWith("-D")) {
                    int indexOfEQ = arg.indexOf('=');
                    if (indexOfEQ < 0) {
                        System.setProperty(arg.substring(2), "");
                    } else {
                        System.setProperty(arg.substring(2, indexOfEQ), arg.substring(indexOfEQ + 1));
                    }
                } else {
                    argList.add(arg);
                }
            }
        }
        return argList.toArray(new String[0]);
    }

    public static boolean isOn(String v) {
        if (v == null) {
            return false;
        }
        return Arrays.asList("Y", "ON", "OK", "YES", "TRUE").contains(v.toUpperCase());
    }
}
