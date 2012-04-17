package me.hatter.tools.jtop.main;

import me.hatter.tools.jtop.agent.AgentInitialization;
import me.hatter.tools.jtop.agent.JDK6AgentLoader;
import me.hatter.tools.jtop.rmi.RmiClient;
import me.hatter.tools.jtop.rmi.interfaces.JStackService;
import me.hatter.tools.jtop.util.ArgsUtil;

public class Main {

    public static void main(String[] args) {
        try {
            args = ArgsUtil.processArgs(args);

            if (System.getProperty("pid") == null) {
                System.out.println("[ERROR] pid is not assigned.");
                System.exit(0);
            }
            int pid = Integer.valueOf(System.getProperty("pid"));
            String jarFilePath = (new AgentInitialization()).findPathToJarFileFromClasspath();
            System.out.println("[INFO] jar file: " + jarFilePath);
            JDK6AgentLoader agentLoader = new JDK6AgentLoader(jarFilePath, String.valueOf(pid));
            agentLoader.loadAgent();

            RmiClient rc = new RmiClient("127.0.0.1", 1127);
            JStackService jss = rc.getJStackService();
            if (jss == null) {
                System.err.println("[ERROR] connect to server failed.");
            } else {
                String[] tis = jss.listThreadInfos();
                for (String ti : tis) {
                    System.out.println(ti);
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] unknow error occured: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
