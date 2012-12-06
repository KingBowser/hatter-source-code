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

import jline.TerminalFactory;
import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.bytes.ByteUtil;
import me.hatter.tools.commons.bytes.ByteUtil.ByteFormat;
import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.color.Font;
import me.hatter.tools.commons.color.Position;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.screen.TermUtils;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.jtop.main.objects.MainOutput;
import me.hatter.tools.jtop.management.JTopMXBean;
import me.hatter.tools.jtop.rmi.RmiClient;
import me.hatter.tools.jtop.rmi.interfaces.JClassLoadingInfo;
import me.hatter.tools.jtop.rmi.interfaces.JGCInfo;
import me.hatter.tools.jtop.rmi.interfaces.JMemoryInfo;
import me.hatter.tools.jtop.rmi.interfaces.JThreadInfo;
import me.hatter.tools.jtop.rmi.interfaces.StackTraceElement;
import me.hatter.tools.jtop.util.EnvUtil;
import me.hatter.tools.jtop.util.console.Color2;
import me.hatter.tools.jtop.util.console.Font2;
import me.hatter.tools.jtop.util.console.Text2;

public class Main {

    public static void main(String[] args) {
        try {
            UnixArgsutil.parseGlobalArgs(args);
            boolean advanced = UnixArgsutil.ARGS.flags().containsAny("A", "advanced");

            HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);

            if (advanced) {
                ClassLoaderUtil.addResourceToSystemClassLoader("/jtop-resources/jline-2.9.jar");
            }

            if (UnixArgsutil.ARGS.args().length == 0) {
                System.out.println("[ERROR] pid is not assigned.");
                usage();
                System.exit(0);
            }

            String pid = UnixArgsutil.ARGS.args()[0];

            RmiClient rc = new RmiClient(pid);
            JTopMXBean jTopMXBean = rc.getJTopMXBean();

            long lastNano = System.nanoTime();
            MainOutput lastMainOutput = null;
            Map<Long, JThreadInfo> lastJThreadInfoMap = null;
            int dumpcount = advanced ? Integer.MAX_VALUE : EnvUtil.getDumpCount();
            for (int c = -1; c < dumpcount; c++) {
                long nano = System.nanoTime();
                MainOutput mainOutput = new MainOutput(c + 1);
                JThreadInfo[] jThreadInfos = jTopMXBean.listThreadInfos();
                Map<Long, JThreadInfo> jThreadInfoMap = jThreadInfoToMap(jThreadInfos);
                if (lastMainOutput == null) {
                    System.out.println("[INFO] First Round");
                } else {
                    if (advanced) {
                        displayRoundA(jTopMXBean, lastNano, lastMainOutput, lastJThreadInfoMap, nano, mainOutput,
                                      jThreadInfos);
                    } else {
                        displayRound(jTopMXBean, lastNano, lastMainOutput, lastJThreadInfoMap, nano, mainOutput,
                                     jThreadInfos);
                    }
                }
                lastNano = nano;
                lastMainOutput = mainOutput;
                lastJThreadInfoMap = jThreadInfoMap;

                if (c < (dumpcount - 1)) {
                    Thread.sleep(EnvUtil.getSleepMillis());
                }
            }
            System.out.println("[INFO] Dump Finish");
        } catch (Exception e) {
            System.err.println("[ERROR] unknow error occured: " + e.getMessage());
            e.printStackTrace();
            System.out.print(TermUtils.RESET);
        }
    }

    private static void displayRoundA(JTopMXBean jTopMXBean, long lastNano, MainOutput lastMainOutput,
                                      Map<Long, JThreadInfo> lastJThreadInfoMap, long nano, MainOutput mainOutput,
                                      JThreadInfo[] jThreadInfos) {
        System.out.print(TermUtils.CLEAR);
        System.out.print(Font.createFont(Position.getPosition(1, 1), null).display(StringUtil.EMPTY));
        System.out.print(TermUtils.MOVE_LEFT1);

        JThreadInfo[] cJThreadInfos = caculateJThreadInfos(jThreadInfos, lastJThreadInfoMap);
        cJThreadInfos = sortJThreadInfos(cJThreadInfos);
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

        String size = EnvUtil.getSize();

        DecimalFormat nf = new DecimalFormat("0.00");
        List<String> outputs = new ArrayList<String>();
        for (int i = 0; ((i < cJThreadInfos.length) && (i < Integer.MAX_VALUE)); i++) {
            JThreadInfo jThreadInfo = cJThreadInfos[i];
            outputs.add(jThreadInfo.getThreadName() //
                        + "  TID=" + jThreadInfo.getThreadId() //
                        + "  STATE=" + jThreadInfo.getThreadState().name() //
                        + "  CPU_TIME=" + TimeUnit.NANOSECONDS.toMillis(jThreadInfo.getCpuTime())//
                        + " (" + nf.format(((double) jThreadInfo.getCpuTime()) * 100 / cost) + "%)" //
                        + "  USER_TIME=" + TimeUnit.NANOSECONDS.toMillis(jThreadInfo.getUserTime()) //
                        + " (" + nf.format(((double) jThreadInfo.getUserTime()) * 100 / cost) + "%)" //
                        + " Allocted: " + toSize(jThreadInfo.getAlloctedBytes(), size));
            int matchCount = 0;
            for (int j = 0; ((j < jThreadInfo.getStackTrace().length) && (matchCount < stacktracetopn)); j++) {
                StackTraceElement stackTrace = jThreadInfo.getStackTrace()[j];
                if (isMatch(stackTrace)) {
                    matchCount++;
                    outputs.add("    " + stackTrace.toString());
                }
            }
            if ((matchCount == 0) && (jThreadInfo.getStackTrace().length > 0)) {
                outputs.add("    ---- all filtered ----");
            }
            outputs.add("");
        }
        int h = TerminalFactory.create().getHeight();
        int w = TerminalFactory.create().getWidth();
        // System.out.println("XXXX:"+h + ":"+w+"/"+outputs.size());
        for (int x = 0; x < outputs.size() && x < h - 1; x++) {
            String s = outputs.get(x);
            if (s.length() > w) {
                s = s.substring(0, w);
            }
            System.out.println(s);
        }
    }

    private static void displayRound(JTopMXBean jTopMXBean, long lastNano, MainOutput lastMainOutput,
                                     Map<Long, JThreadInfo> lastJThreadInfoMap, long nano, MainOutput mainOutput,
                                     JThreadInfo[] jThreadInfos) {
        // display to console
        System.out.println("NEW ROUND =================================================================== ");
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

        String size = EnvUtil.getSize();

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
                        + " (" + nf.format(((double) jThreadInfo.getUserTime()) * 100 / cost) + "%)" //
                        + " Allocted: " + toSize(jThreadInfo.getAlloctedBytes(), size));
            int matchCount = 0;
            for (int j = 0; ((j < jThreadInfo.getStackTrace().length) && (matchCount < stacktracetopn)); j++) {
                StackTraceElement stackTrace = jThreadInfo.getStackTrace()[j];
                if (isMatch(stackTrace)) {
                    matchCount++;
                    outputs.add("    " + stackTrace.toString());
                }
            }
            if ((matchCount == 0) && (jThreadInfo.getStackTrace().length > 0)) {
                outputs.add("    ---- all filtered ----");
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

        if (!UnixArgsutil.ARGS.flags().contains("summaryoff")) {
            JMemoryInfo jMemoryInfo = jTopMXBean.getMemoryInfo();
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

            JGCInfo[] jgcInfos = jTopMXBean.getGCInfos();
            for (JGCInfo jgcInfo : jgcInfos) {
                System.out.println("GC " + jgcInfo.getName() //
                                   + "  " + (jgcInfo.getIsValid() ? "VALID" : "NOT_VALID") //
                                   + "  " + Arrays.asList(jgcInfo.getMemoryPoolNames()) //
                                   + "  GC=" + jgcInfo.getCollectionCount() //
                                   + "  GCT=" + jgcInfo.getCollectionTime() //
                );
            }

            JClassLoadingInfo jClassLoadingInfo = jTopMXBean.getClassLoadingInfo();
            System.out.println("ClassLoading" //
                               + " LOADED=" + jClassLoadingInfo.getLoadedClassCount() //
                               + "  TOTAL_LOADED=" + jClassLoadingInfo.getTotalLoadedClassCount() //
                               + "  UNLOADED=" + jClassLoadingInfo.getUnloadedClassCount() //
            );
        }

        System.out.println("Total threads: " //
                           + Text2.createText(getFont(mainOutput, cJThreadInfos.length,
                                                      mainOutput.getTotalThreadCount()),
                                              String.valueOf(cJThreadInfos.length))
                           + Text2.createText(getFont(mainOutput, totalCpu, lastMainOutput.getTotalCpuTime()),
                                              "  CPU=" + TimeUnit.NANOSECONDS.toMillis(totalCpu) + " ("
                                                      + nf.format(((double) totalCpu) * 100 / cost) + "%)")//
                           + Text2.createText(getFont(mainOutput, totalUser, lastMainOutput.getTotalUserTime()),
                                              "  USER=" + TimeUnit.NANOSECONDS.toMillis(totalUser) //
                                                      + " (" + nf.format(((double) totalUser) * 100 / cost) + "%)") //
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

    static boolean isMatch(StackTraceElement element) {
        List<String> excludes = UnixArgsutil.ARGS.kvalues("excludes");
        List<String> includes = UnixArgsutil.ARGS.kvalues("includes");
        if (CollectionUtil.isEmpty(excludes)) {
            return true;
        }
        boolean isMatch = false;
        for (String e : excludes) {
            if (isMatchOne(element.toString(), e)) {
                isMatch = true;
            }
        }
        if (!isMatch) {
            return true;
        }
        if (!CollectionUtil.isEmpty(includes)) {
            for (String i : includes) {
                if (isMatchOne(element.toString(), i)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean isMatchOne(String ele, String pattern) {
        if (pattern.startsWith("^")) {
            if (ele.toString().startsWith(pattern.substring(1))) {
                return true;
            }
        } else {
            if (ele.toString().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    static JThreadInfo[] sortJThreadInfos(JThreadInfo[] cJThreadInfos) {
        boolean isSortMem = EnvUtil.getSortMem();
        if (isSortMem) {
            Arrays.sort(cJThreadInfos, new Comparator<JThreadInfo>() {

                public int compare(JThreadInfo o1, JThreadInfo o2) {
                    int rMem = Long.valueOf(o2.getAlloctedBytes()).compareTo(Long.valueOf(o1.getAlloctedBytes()));
                    if (rMem != 0) {
                        return rMem;
                    }
                    return o2.getThreadName().compareTo(o1.getThreadName());
                }
            });
        } else {
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
        }
        return cJThreadInfos;
    }

    static Font2 getFont(MainOutput mainOutput, long value, long lastValue) {
        if (value == lastValue) {
            return null;
        }
        if (mainOutput.getRound() == 0) {
            return null;
        }
        if (!EnvUtil.getColor()) {
            return null;
        }
        if (value > lastValue) {
            return Font2.createFont(Color2.RED, false);
        } else {
            return Font2.createFont(Color2.GREEN, false);
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
                                                   jThreadInfo.getUserTime() - lastJThreadInfo.getUserTime(),
                                                   jThreadInfo.getAlloctedBytes() - lastJThreadInfo.getAlloctedBytes());
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

    static String toSize(long b, String s) {
        return ByteUtil.formatBytes(ByteFormat.fromString(s), b);
    }

    static void usage() {
        System.out.println("Usage[b121206]:");
        System.out.println("java -jar jtop.jar [options] <pid> [<interval> [<count>]]");
        System.out.println("-OR-");
        System.out.println("java -cp jtop.jar jtop [options] <pid> [<interval> [<count>]]");
        System.out.println("    -size <B|K|M|G|H>             Size, case insensitive (default: B, H for human)");
        System.out.println("    -thread <N>                   Thread Top N (default: 5)");
        System.out.println("    -stack <N>                    Stacktrace Top N (default: 8)");
        System.out.println("    -excludes                     Excludes (string.contains)");
        System.out.println("    -includes                     Includes (string.contains, excludes than includes)");
        System.out.println("    --color                       Display color (default: off)");
        System.out.println("    --sortmem                     Sort by memory allocted (default: off)");
        System.out.println("    --summaryoff                  Do not display summary (default: off)");
        System.out.println();
    }
}
