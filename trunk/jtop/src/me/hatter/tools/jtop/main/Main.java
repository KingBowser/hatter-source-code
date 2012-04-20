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
import me.hatter.tools.jtop.main.objects.MainOutput;
import me.hatter.tools.jtop.rmi.RmiClient;
import me.hatter.tools.jtop.rmi.exception.ServiceNotStartedException;
import me.hatter.tools.jtop.rmi.interfaces.JClassLoadingInfo;
import me.hatter.tools.jtop.rmi.interfaces.JGCInfo;
import me.hatter.tools.jtop.rmi.interfaces.JMemoryInfo;
import me.hatter.tools.jtop.rmi.interfaces.JStackService;
import me.hatter.tools.jtop.rmi.interfaces.JThreadInfo;
import me.hatter.tools.jtop.util.ArgsUtil;
import me.hatter.tools.jtop.util.EnvUtil;
import me.hatter.tools.jtop.util.console.Color;
import me.hatter.tools.jtop.util.console.Font;
import me.hatter.tools.jtop.util.console.Text;

public class Main {

    public static void main(String[] args) {
        try {
            args = ArgsUtil.processArgs(args);

            if (System.getProperty("pid") == null) {
                System.out.println("[ERROR] pid is not assigned.");
                usage();
                System.exit(0);
            }
            RmiClient rc = new RmiClient("127.0.0.1", EnvUtil.getPort());
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
                                   + "  REQUIRE_PID=" + EnvUtil.getPort() //
                                   + "  ACTURE_PID=" + pid);
                String jarFilePath = (new AgentInitialization()).findPathToJarFileFromClasspath();
                System.out.println("[ERROR] The target VM PORT="
                                   + (new JDK6AgentLoader(jarFilePath, String.valueOf(EnvUtil.getPort()))).getVMProperty("jtop.port"));
                System.exit(0);
            }

            long lastNano = System.nanoTime();
            MainOutput lastMainOutput = null;
            Map<Long, JThreadInfo> lastJThreadInfoMap = null;
            int dumpcount = EnvUtil.getDumpCount();
            for (int c = -1; c < dumpcount; c++) {
                long nano = System.nanoTime();
                MainOutput mainOutput = new MainOutput(c + 1);
                JThreadInfo[] jThreadInfos = jStackService.listThreadInfos();
                Map<Long, JThreadInfo> jThreadInfoMap = jThreadInfoToMap(jThreadInfos);
                if (lastMainOutput == null) {
                    System.out.println("[INFO] First Round");
                } else {
                    // display to console
                    System.out.println("NEW ROUND ================================================== ");
                    JThreadInfo[] cJThreadInfos = caculateJThreadInfos(jThreadInfos, lastJThreadInfoMap);
                    cJThreadInfos = sortJThreadInfos(cJThreadInfos);
                    int threadtopn = EnvUtil.getThreadTopN();
                    int stacktracetopn = EnvUtil.getStacktraceTopN();

                    long cost = nano - lastNano;
                    long totalCpu = 0;
                    long totalUser = 0;
                    for (JThreadInfo jThreadInfo : cJThreadInfos) {
                        totalCpu += jThreadInfo.getCpuTime();
                        totalUser += jThreadInfo.getUserTime();
                    }
                    mainOutput.setTotalThreadCount(cJThreadInfos.length);
                    mainOutput.setTotalCpuTime(totalCpu);
                    mainOutput.setTotalUserTime(totalUser);

                    DecimalFormat nf = new DecimalFormat("0.00");
                    List<String> outputs = new ArrayList<String>();
                    for (int i = 0; ((i < cJThreadInfos.length) && (i < threadtopn)); i++) {
                        JThreadInfo jThreadInfo = cJThreadInfos[i];
                        outputs.add(jThreadInfo.getThreadName() //
                                    + "  TID=" + jThreadInfo.getThreadId() //
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

                    String size = EnvUtil.getSize();
                    JMemoryInfo jMemoryInfo = jStackService.getMemoryInfo();
                    mainOutput.setjMemoryInfo(jMemoryInfo);
                    System.out.println("Heap Memory:" //
                                       + " INIT=" + toSize(jMemoryInfo.getHeap().getInit(), size) //
                                       + "  USED=" + toSize(jMemoryInfo.getHeap().getUsed(), size) //
                                       + "  COMMITED=" + toSize(jMemoryInfo.getHeap().getCommitted(), size) //
                                       + "  MAX=" + toSize(jMemoryInfo.getHeap().getMax(), size) //
                    );
                    System.out.println("NonHeap Memory:" //
                                       + " INIT=" + toSize(jMemoryInfo.getNonHeap().getInit(), size) //
                                       + "  USED=" + toSize(jMemoryInfo.getNonHeap().getUsed(), size) //
                                       + "  COMMITED=" + toSize(jMemoryInfo.getNonHeap().getCommitted(), size) //
                                       + "  MAX=" + toSize(jMemoryInfo.getNonHeap().getMax(), size) //
                    );

                    JGCInfo[] jgcInfos = jStackService.getGCInfos();
                    for (JGCInfo jgcInfo : jgcInfos) {
                        System.out.println("GC " + jgcInfo.getName() //
                                           + "  " + (jgcInfo.isValid() ? "VALID" : "NOT_VALID") //
                                           + "  " + Arrays.asList(jgcInfo.getMemoryPoolNames()) //
                                           + "  GC=" + jgcInfo.getCollectionCount() //
                                           + "  GCT=" + jgcInfo.getCollectionTime() //
                        );
                    }

                    JClassLoadingInfo jClassLoadingInfo = jStackService.getClassLoadingInfo();
                    System.out.println("ClassLoading" //
                                       + " LOADED=" + jClassLoadingInfo.getLoadedClassCount() //
                                       + "  TOTAL_LOADED=" + jClassLoadingInfo.getTotalLoadedClassCount() //
                                       + "  UNLOADED=" + jClassLoadingInfo.getUnloadedClassCount() //
                    );

                    System.out.println("Total threads: " //
                                       + Text.createText(getFont(mainOutput, cJThreadInfos.length,
                                                                 mainOutput.getTotalThreadCount()),
                                                         String.valueOf(cJThreadInfos.length))
                                       + Text.createText(getFont(mainOutput, totalCpu, lastMainOutput.getTotalCpuTime()),
                                                         "  CPU=" + TimeUnit.NANOSECONDS.toMillis(totalCpu) + " ("
                                                                 + nf.format(((double) totalCpu) * 100 / cost) + "%)")//
                                       + Text.createText(getFont(mainOutput, totalUser,
                                                                 lastMainOutput.getTotalUserTime()),
                                                         "  USER="
                                                                 + TimeUnit.NANOSECONDS.toMillis(totalUser) //
                                                                 + " (" + nf.format(((double) totalUser) * 100 / cost)
                                                                 + "%)") //
                    );
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
                lastMainOutput = mainOutput;
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

    static JThreadInfo[] sortJThreadInfos(JThreadInfo[] cJThreadInfos) {
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
        return cJThreadInfos;
    }

    static Font getFont(MainOutput mainOutput, long value, long lastValue) {
        if (value == lastValue) {
            return null;
        }
        if (mainOutput.getRound() == 0) {
            return null;
        }
        if (value > lastValue) {
            return Font.createFont(Color.RED, false);
        } else {
            return Font.createFont(Color.GREEN, false);
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

    static String toSize(long b, String s) {
        if (s == null) {
            return String.valueOf(b);
        }
        DecimalFormat nf = new DecimalFormat("0.00");
        char c = s.toLowerCase().trim().charAt(0);
        if (c == 'h') {
            if (b > 1024 * 1024 * 1024) {
                c = 'g';
            } else if (b > 1024 * 1024) {
                c = 'm';
            } else if (b > 1024) {
                c = 'k';
            }
        }
        switch (c) {
            case 'k':
                return nf.format(((double) b) / 1024) + "K";
            case 'm':
                return nf.format(((double) b) / 1024 / 1024) + "M";
            case 'g':
                return nf.format(((double) b) / 1024 / 1024 / 1024) + "G";
            default:
                return String.valueOf(b);
        }
    }

    static void usage() {
        System.out.println("Usage:");
        System.out.println("java -jar jtop.jar [args]");
        System.out.println("-OR-");
        System.out.println("java -cp jtop.jar jtop [args]");
        System.out.println("    -Dpid=<PID>                   Process ID");
        System.out.println("    -Dport=<PORT>                 Port (default: 1127)");
        System.out.println("    -Dsize=B|K|M|G|H              Size, case insensitive (default: B, H for human)");
        System.out.println("    -Ddumpcount=<COUNT>           Dump Count (default: 1)");
        System.out.println("    -Dsleepmillis=<MILLIS>        Sleep Mills (default: 2000)");
        System.out.println("    -Dthreadtopn=<N>              Thread Top N (default: 5)");
        System.out.println("    -Dstacktracetopn=<N>          Stacktrace Top N (default: 8)");
        System.out.println();
    }
}
