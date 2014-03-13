package me.hatter.tools.iapp.agent;

import java.lang.instrument.Instrumentation;

import me.hatter.tools.commons.agent.AgentArgs;
import me.hatter.tools.commons.log.LogUtil;

public class Agent {

    private static Instrumentation instrumentation;
    private static AgentArgs       agentArgs;

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        if (instrumentation != null) {
            LogUtil.warn("Areadly invoked: premain");
            return;
        }
        invokemain(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        if (instrumentation != null) {
            LogUtil.warn("Areadly invoked: agentmain");
            return;
        }
        instrumentation = inst;
        invokemain(agentArgs, inst);
    }

    public static void invokemain(String agentArgs, Instrumentation inst) throws Exception {
        instrumentation = inst;
        Agent.agentArgs = new AgentArgs(agentArgs);
        LogUtil.info("void " + Agent.class.getName() + "#invokemain(String, Instrumentation), with: "
                     + Agent.agentArgs.getOriArgs());
        // TODO ...
    }
}
