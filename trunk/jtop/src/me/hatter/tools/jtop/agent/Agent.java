package me.hatter.tools.jtop.agent;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

import me.hatter.tools.commons.agent.AgentUtil;
import me.hatter.tools.jtop.rmi.RmiServer;

public class Agent {

    static final String            javaSpecVersion = System.getProperty("java.specification.version");
    static final boolean           jdk6OrLater     = Arrays.asList("1.6", "1.7").contains(javaSpecVersion);
    private static Instrumentation instrumentation;
    private static int             serverPort;

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        domain(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        domain(agentArgs, inst);
    }

    public static void domain(String agentArgs, Instrumentation inst) throws Exception {
        if (instrumentation == null) {
            instrumentation = inst;
        }
        System.out.println("[INFO] " + Agent.class.getName() + "#domain(" + agentArgs + ", " + inst + ")");
        String port = AgentUtil.parseAgentArgsMap(agentArgs).get("port");
        serverPort = Integer.valueOf((port == null) ? "1127" : port);

        RmiServer.startup(serverPort);
    }
}