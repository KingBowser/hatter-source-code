package me.hatter.tools.jflag;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.jmx.RemoteManagementFactory;
import me.hatter.tools.commons.jmx.RemoteManagementTool;
import me.hatter.tools.commons.jvm.HotSpotAttachTool;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.string.StringUtil;
import sun.tools.attach.HotSpotVirtualMachine;

import com.sun.tools.attach.VirtualMachine;

public class JFlag {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        UnixArgsutil.parseGlobalArgs(args);

        String flags = IOUtil.readResourceToString(JFlag.class, "flags.txt");
        List<Flag> flagList = FlagParserUtil.parseFlagList(flags);

        if ((UnixArgsutil.ARGS.kvalue("show") != null) && (UnixArgsutil.ARGS.args().length == 0)) {
            flags(flagList);
        }
        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }
        String pid = UnixArgsutil.ARGS.args()[0];

        HotSpotAttachTool attach = new HotSpotAttachTool(pid);
        attach.attach();
        Set<String> ignoreSet = new HashSet<String>();
        ignoreSet.add("FLSLargestBlockCoalesceProximity");
        ignoreSet.add("CMSSmallCoalSurplusPercent");
        ignoreSet.add("CMSLargeCoalSurplusPercent");
        ignoreSet.add("CMSSmallSplitSurplusPercent");
        ignoreSet.add("CMSLargeSplitSurplusPercent");
        System.out.println("Default ignore: " + ignoreSet);
        try {
            VirtualMachine vm = attach.getVM();
            HotSpotVirtualMachine hvm = (HotSpotVirtualMachine) vm;
            try {
                for (Flag flag : flagList) {
                    if (ignoreSet.contains(flag.getName())) continue;
                    if (!filterFlag(flag)) continue;

                    String result = IOUtil.readToStringAndClose(hvm.printFlag(flag.getName()), "UTF-8").trim();
                    boolean notExists = result.toLowerCase().contains("no such flag");
                    if (notExists) {
                        if (UnixArgsutil.ARGS.flags().contains("show-not-exists")) {
                            System.out.println(result);
                        }
                    } else {
                        System.out.println(result);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            attach.detach();
        }
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

    private static boolean filterFlag(Flag flag) {
        if (flag == null) {
            return false;
        }
        String show = UnixArgsutil.ARGS.kvalue("show");
        boolean showAll = ("ALL".equals(show) || (show == null));
        if (showAll || flag.getName().toLowerCase().contains(show.toLowerCase())) {
            return true;
        }
        return false;
    }

    private static void flags(List<Flag> flagList) {
        System.out.println(StringUtil.paddingSpaceRight("FlagName", 40) + " "
                           + StringUtil.paddingSpaceRight("Type", 20) + "Runtime");
        System.out.println(StringUtil.repeat("-", 70));
        for (Flag flag : flagList) {
            if (filterFlag(flag)) {
                System.out.println(StringUtil.paddingSpaceRight(flag.getName(), 40) + " "
                                   + StringUtil.paddingSpaceRight(flag.getType().getName(), 20)
                                   + flag.getRuntime().getName());
            }
        }
        System.exit(0);
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar jflagall.jar [options] <pid>");
        System.out.println("    -show <flags>           show flags('ALL' show all)");
        System.out.println("    -runt <option>          collection(default product)");
        System.out.println("          product           runtime product");
        System.out.println("          develop           runtime develop");
        System.out.println("    --show-not-exists       show not exists flag(s)");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }
}
