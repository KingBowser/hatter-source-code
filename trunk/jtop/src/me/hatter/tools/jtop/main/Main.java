package me.hatter.tools.jtop.main;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.jtop.agent.AgentInitialization;
import me.hatter.tools.jtop.agent.JDK6AgentLoader;
import me.hatter.tools.jtop.rmi.RmiClient;
import me.hatter.tools.jtop.rmi.exception.ServiceNotStartedException;
import me.hatter.tools.jtop.rmi.interfaces.JStackService;
import me.hatter.tools.jtop.rmi.interfaces.JThreadInfo;
import me.hatter.tools.jtop.util.ArgsUtil;

public class Main {

    public static void main(String[] args) {
        try {
            args = ArgsUtil.processArgs(args);

            if (System.getProperty("pid") == null) {
                System.out.println("[ERROR] pid is not assigned.");
                System.exit(0);
            }
            RmiClient rc = new RmiClient("127.0.0.1", 1127);
            if (!tryConnect(rc)) {
                int pid = Integer.valueOf(System.getProperty("pid"));
                attachAgent(pid);
            }

            JStackService jStackService = rc.getJStackService();
            if (jStackService == null) {
                System.err.println("[ERROR] connect to server failed.");
                System.exit(0);
            }
            JThreadInfo[] lastJThreadInfos = null;
            Map<Long, JThreadInfo> lastJThreadInfoMap = null;
            while (true) {
                JThreadInfo[] jThreadInfos = jStackService.listThreadInfos();
                Map<Long, JThreadInfo> jThreadInfoMap = jThreadInfoToMap(jThreadInfos);
                if (lastJThreadInfos != null) {
                    // display to console
                    System.out.println("NEW ROUND ================================================== ");
                    JThreadInfo[] cJThreadInfos = caculateJThreadInfos(jThreadInfos, lastJThreadInfoMap);
                    Arrays.sort(cJThreadInfos, new Comparator<JThreadInfo>() {

                        public int compare(JThreadInfo o1, JThreadInfo o2) {
                            int rCpu = Long.valueOf(o2.getCpuTime()).compareTo(Long.valueOf(o1.getCpuTime()));
                            if (rCpu != 0) {
                                return rCpu;
                            }
                            int rUser = Long.valueOf(o2.getUserTime()).compareTo(Long.valueOf(o1.getUserTime()));
                            if (rUser != 0) {
                                return rUser;
                            }
                            return o2.getThreadName().compareTo(o1.getThreadName());
                        }
                    });
                    int threadtopn = Integer.valueOf(System.getProperty("threadtopn", "10"));
                    int stacktracetopn = Integer.valueOf(System.getProperty("stacktracetopn", "6"));

                    for (int i = 0; ((i < cJThreadInfos.length) && (i < threadtopn)); i++) {
                        JThreadInfo jThreadInfo = cJThreadInfos[i];
                        System.out.println(jThreadInfo.getThreadName() //
                                           + "  PID=" + jThreadInfo.getThreadId() //
                                           + "  STATE=" + jThreadInfo.getThreadState().name() //
                                           + "  CPU_TIME=" + jThreadInfo.getCpuTime() //
                                           + "  USER_TIME=" + jThreadInfo.getUserTime());
                        for (int j = 0; ((j < jThreadInfo.getStackTrace().length) && (j < stacktracetopn)); j++) {
                            StackTraceElement stackTrace = jThreadInfo.getStackTrace()[j];
                            System.out.println("\t" + stackTrace.toString());
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
                lastJThreadInfos = jThreadInfos;
                lastJThreadInfoMap = jThreadInfoMap;

                Thread.sleep(Long.parseLong(System.getProperty("sleepmillis", "2000")));
            }
        } catch (Exception e) {
            System.err.println("[ERROR] unknow error occured: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static JThreadInfo[] caculateJThreadInfos(JThreadInfo[] jThreadInfos, Map<Long, JThreadInfo> lastJThreadInfoMap) {
        JThreadInfo[] cjThreadInfos = new JThreadInfo[jThreadInfos.length];
        for (int i = 0; i < jThreadInfos.length; i++) {
            JThreadInfo jThreadInfo = jThreadInfos[i];
            JThreadInfo lastJThreadInfo = lastJThreadInfoMap.get(Long.valueOf(jThreadInfo.getThreadId()));
            if (lastJThreadInfo == null) {
                cjThreadInfos[i] = jThreadInfo;
            } else {
                cjThreadInfos[i] = new JThreadInfo(jThreadInfo,
                                                   jThreadInfo.getCpuTime() - lastJThreadInfo.getCpuTime(),
                                                   jThreadInfo.getUserTime() - lastJThreadInfo.getUserTime());
            }
        }
        return cjThreadInfos;
    }

    static Map<Long, JThreadInfo> jThreadInfoToMap(JThreadInfo[] jThreadInfos) {
        Map<Long, JThreadInfo> jThreadInfoMap = new HashMap<Long, JThreadInfo>();
        for (JThreadInfo jThreadInfo : jThreadInfos) {
            jThreadInfoMap.put(Long.valueOf(jThreadInfo.getThreadId()), jThreadInfo);
        }
        return jThreadInfoMap;
    }

    static boolean tryConnect(RmiClient rc) {
        try {
            rc.getJStackService();
            return true;
        } catch (ServiceNotStartedException snse) {
            return false;
        }
    }

    static void attachAgent(int pid) {
        String jarFilePath = (new AgentInitialization()).findPathToJarFileFromClasspath();
        System.out.println("[INFO] jar file: " + jarFilePath);
        JDK6AgentLoader agentLoader = new JDK6AgentLoader(jarFilePath, String.valueOf(pid));
        agentLoader.loadAgent();
    }
}
