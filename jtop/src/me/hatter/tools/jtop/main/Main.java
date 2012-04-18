package me.hatter.tools.jtop.main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
                usage();
                System.exit(0);
            }
            RmiClient rc = new RmiClient("127.0.0.1", Integer.parseInt(System.getProperty("port", "1127")));
            if (!tryConnect(rc)) {
                int pid = Integer.valueOf(System.getProperty("pid"));
                attachAgent(pid);
            }

            JStackService jStackService = rc.getJStackService();
            if (jStackService == null) {
                System.err.println("[ERROR] connect to server failed.");
                System.exit(0);
            }
            String pid = jStackService.getProcessId();
            if (!pid.equals(System.getProperty("pid"))) {
                System.out.println("[ERROR] Remote server's pid not match, PORT=" + rc.getPort() //
                                   + "  REQUIRE_PID=" + System.getProperty("pid") //
                                   + "  ACTURE_PID=" + pid);
                System.exit(0);
            }

            long lastNano = System.nanoTime();
            JThreadInfo[] lastJThreadInfos = null;
            Map<Long, JThreadInfo> lastJThreadInfoMap = null;
            int dumpcount = Integer.parseInt(System.getProperty("dumpcount", "1"));
            for (int c = -1; c < dumpcount; c++) {
                long nano = System.nanoTime();
                JThreadInfo[] jThreadInfos = jStackService.listThreadInfos();
                Map<Long, JThreadInfo> jThreadInfoMap = jThreadInfoToMap(jThreadInfos);
                if (lastJThreadInfos == null) {
                    System.out.println("[INFO] First Round");
                } else {
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
                    int threadtopn = Integer.valueOf(System.getProperty("threadtopn", "5"));
                    int stacktracetopn = Integer.valueOf(System.getProperty("stacktracetopn", "8"));

                    long cost = nano - lastNano;
                    long totalCpu = 0;
                    long totalUser = 0;
                    DecimalFormat nf = new DecimalFormat("0.00");
                    List<String> outputs = new ArrayList<String>();
                    for (int i = 0; ((i < cJThreadInfos.length) && (i < threadtopn)); i++) {
                        JThreadInfo jThreadInfo = cJThreadInfos[i];
                        totalCpu += jThreadInfo.getCpuTime();
                        totalUser += jThreadInfo.getUserTime();
                        outputs.add(jThreadInfo.getThreadName() //
                                    + "  PID=" + jThreadInfo.getThreadId() //
                                    + "  STATE=" + jThreadInfo.getThreadState().name() //
                                    + "  CPU_TIME=" + TimeUnit.NANOSECONDS.toMillis(jThreadInfo.getCpuTime())//
                                    + " (" + nf.format(((double) jThreadInfo.getCpuTime()) * 100 / cost) + "%)" //
                                    + "  USER_TIME=" + TimeUnit.NANOSECONDS.toMillis(jThreadInfo.getUserTime()) //
                                    + " (" + nf.format(((double) jThreadInfo.getUserTime()) * 100 / cost) + "%)");
                        for (int j = 0; ((j < jThreadInfo.getStackTrace().length) && (j < stacktracetopn)); j++) {
                            StackTraceElement stackTrace = jThreadInfo.getStackTrace()[j];
                            outputs.add("\t" + stackTrace.toString());
                        }
                        outputs.add("");
                    }

                    Map<Thread.State, AtomicInteger> stateMap = new HashMap<Thread.State, AtomicInteger>();
                    for (JThreadInfo jThreadInfo : cJThreadInfos) {
                        Thread.State state = jThreadInfo.getThreadState();
                        if (stateMap.containsKey(state)) {
                            stateMap.get(state).incrementAndGet();
                        } else {
                            stateMap.put(state, new AtomicInteger(1));
                        }
                    }

                    System.out.println("Total threads: " + cJThreadInfos.length //
                                       + "  CPU=" + TimeUnit.NANOSECONDS.toMillis(totalCpu) //
                                       + " (" + nf.format(((double) totalCpu) * 100 / cost) + "%)" //
                                       + "  USER=" + TimeUnit.NANOSECONDS.toMillis(totalUser) //
                                       + " (" + nf.format(((double) totalUser) * 100 / cost) + "%)");
                    for (Thread.State state : Thread.State.values()) {
                        AtomicInteger ai = stateMap.get(state);
                        ai = (ai == null) ? new AtomicInteger(0) : ai;
                        System.out.print(state + "=" + ai.get() + "  ");
                    }
                    System.out.println();
                    for (String s : outputs) {
                        System.out.println(s);
                    }
                    System.out.println();
                }
                lastNano = nano;
                lastJThreadInfos = jThreadInfos;
                lastJThreadInfoMap = jThreadInfoMap;

                if (c < (dumpcount - 1)) {
                    Thread.sleep(Long.parseLong(System.getProperty("sleepmillis", "2000")));
                }
            }
            System.out.println("[INFO] Dump Finish");
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

    static void usage() {
        System.out.println("Usage:");
        System.out.println("java -jar jtop.jar [args]");
        System.out.println("-OR-");
        System.out.println("java -cp jtop.jar jtop [args]");
        System.out.println("    -Dpid=<PID>                   Process ID");
        System.out.println("    -Dport=<PORT>                 Port (default: 1127)");
        System.out.println("    -Ddumpcount=<COUNT>           Dump Count (default: 1)");
        System.out.println("    -Dsleepmillis=<MILLIS>        Sleep Mills (default: 2000)");
        System.out.println("    -Dthreadtopn=<N>              Thread Top N (default: 5)");
        System.out.println("    -Dstacktracetopn=<N>          Stacktrace Top N (default: 8)");
        System.out.println();
    }
}
