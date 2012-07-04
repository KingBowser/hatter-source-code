package me.hatter.tools.jtop.agent;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.jtop.rmi.RmiServer;

public class Agent {

    static final String            javaSpecVersion = System.getProperty("java.specification.version");
    static final boolean           jdk6OrLater     = Arrays.asList("1.6", "1.7").contains(javaSpecVersion);
    private static Instrumentation instrumentation;
    private static int             serverPort;

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        if (instrumentation != null) {
            System.out.println("[WARN] Areadly inited, port=" + serverPort);
            return;
        }
        instrumentation = inst;
        serverPort = Integer.valueOf(getValue(agentArgs, "port", "1127"));
        System.out.println("[INFO] " + Agent.class.getName() + "#premain(" + agentArgs + ", " + inst + ")");
        RmiServer.startup(serverPort);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        if (instrumentation != null) {
            System.out.println("[WARN] Areadly inited, port=" + serverPort);
            return;
        }
        instrumentation = inst;
        serverPort = Integer.valueOf(getValue(agentArgs, "port", "1127"));
        System.out.println("[INFO] " + Agent.class.getName() + "#agentmain(" + agentArgs + ", " + inst + ")");
        RmiServer.startup(serverPort);
    }

    public static String getValue(String str, String key, String def) {
        Map<String, String> map = parseMap(str);
        String val = map.get(key);
        return (val == null) ? def : val;
    }

    public static Map<String, String> parseMap(String str) {
        Map<String, String> map = new HashMap<String, String>();
        if (str != null) {
            String[] arr = str.split(",");
            for (String s : arr) {
                int indexOfEq = s.indexOf('=');
                if (indexOfEq == -1) {
                    map.put(s, "");
                } else if (indexOfEq > 0) {
                    map.put(s.substring(0, indexOfEq), s.substring(indexOfEq + 1));
                } else {
                    System.out.println("[WARN] parameter illegal: " + s);
                }
            }
        }
        return map;
    }
}
