package me.hatter.tools.jprop.agent;

import java.lang.instrument.Instrumentation;

import sun.misc.VMSupport;

import me.hatter.tools.commons.agent.AgentArgs;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.jprop.management.AgentPropertyImpl;
import me.hatter.tools.jprop.management.PropertyImpl;

public class Agent {

    public static final String     PROPERTY_MXBEAN_PROPERTY_KEY = "me.hatter.management.init.Property";

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
        if (VMSupport.getAgentProperties().getProperty(PROPERTY_MXBEAN_PROPERTY_KEY) == null) {
            PropertyImpl.registerMXBean();
            AgentPropertyImpl.registerMXBean();
            VMSupport.getAgentProperties().setProperty(PROPERTY_MXBEAN_PROPERTY_KEY, Boolean.TRUE.toString());
        }
    }
}
