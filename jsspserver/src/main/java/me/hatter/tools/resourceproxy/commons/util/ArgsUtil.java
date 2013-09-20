package me.hatter.tools.resourceproxy.commons.util;

import java.util.ArrayList;
import java.util.List;

public class ArgsUtil {

    public static String[] processArgs(String[] args) {
        List<String> argList = new ArrayList<String>();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("-D")) {
                    int indexOfEQ = arg.indexOf('=');
                    if (indexOfEQ > 2) {
                        String k = arg.substring(2, indexOfEQ);
                        String v = arg.substring(indexOfEQ + 1);
                        System.setProperty(k, v);
                        continue;
                    }
                    if (indexOfEQ < 0) {
                        System.setProperty(arg.substring(2), "");
                        continue;
                    }
                }
                argList.add(arg);
            }
        }
        return argList.toArray(new String[0]);
    }
}
