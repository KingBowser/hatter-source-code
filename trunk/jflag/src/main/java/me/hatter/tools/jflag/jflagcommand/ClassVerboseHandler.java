package me.hatter.tools.jflag.jflagcommand;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jmx.RemoteManagementFactory;
import me.hatter.tools.commons.jmx.RemoteManagementTool;
import me.hatter.tools.jflag.JFlagCommand;
import me.hatter.tools.jflag.JFlagCommandHandler;

public class ClassVerboseHandler implements JFlagCommandHandler {

    public void handle(JFlagCommand command, Boolean isOn, String args) {
        String pid = UnixArgsutil.ARGS.args()[0];
        boolean isOnB = isOn.booleanValue();
        setTraceClassLoading(pid, isOnB);
    }

    private static void setTraceClassLoading(String pid, boolean isOn) {
        RemoteManagementTool tool = new RemoteManagementTool(pid);
        try {
            RemoteManagementFactory factory = tool.getManagementFactory();
            factory.getClassLoadingMXBean().setVerbose(isOn);
        } finally {
            tool.close();
        }
    }
}
