package me.hatter.tools.jtop.agent;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

import me.hatter.tools.jtop.rmi.RmiServer;

public class Agent {

    static final String            javaSpecVersion = System.getProperty("java.specification.version");
    static final boolean           jdk6OrLater     = Arrays.asList("1.6", "1.7").contains(javaSpecVersion);
    private static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        instrumentation = inst;
        System.out.println("[INFO] " + Agent.class.getName() + "#premain(String, Instrumentation)");
        RmiServer.startup();
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        instrumentation = inst;
        System.out.println("[INFO] " + Agent.class.getName() + "#agentmain(String, Instrumentation)");
        RmiServer.startup();
    }

    public static void initializeIfNeeded() {
        if (instrumentation == null) {
            new AgentInitialization().initializeAccordingToJDKVersion();
        }
    }
}
