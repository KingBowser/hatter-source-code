package me.hatter.tools.histoana;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.bytes.ByteUtil;
import me.hatter.tools.commons.bytes.ByteUtil.ByteFormat;
import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.jvm.HotSpotAttachTool;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.number.LongUtil;
import me.hatter.tools.commons.string.StringUtil;
import sun.tools.attach.HotSpotVirtualMachine;

import com.sun.tools.attach.VirtualMachine;

public class HistoAna {

    private static final String LIVE_OBJECTS_OPTION = "-live";
    private static final String ALL_OBJECTS_OPTION  = "-all";

    public static class Args {

        public int     jpid;
        public long    interval    = 5000;
        public long    count       = Long.MAX_VALUE;

        public long    topcount    = 20;
        public boolean orderbysize = true;
        public boolean live        = false;
    }

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        UnixArgsutil.parseGlobalArgs(args);
        final Args histoAnaArgs = parseArgs();

        HotSpotAttachTool attachTool = new HotSpotAttachTool(String.valueOf(histoAnaArgs.jpid));
        attachTool.attach();

        try {
            ClassCountSizeMap lastCCSM = HistoParser.parseHisto(heapHisto(attachTool.getVM(), histoAnaArgs));
            for (long loop = 0; loop < histoAnaArgs.count; loop++) {
                Thread.sleep((histoAnaArgs.interval < 0) ? 0 : histoAnaArgs.interval);
                ClassCountSizeMap ccsm = HistoParser.parseHisto(heapHisto(attachTool.getVM(), histoAnaArgs));

                List<ClassCountSize> diff = ClassCountSizeMap.diff(lastCCSM, ccsm);
                System.out.println("---- histo diff ----");
                Collections.sort(diff, new Comparator<ClassCountSize>() {

                    public int compare(ClassCountSize o0, ClassCountSize o1) {
                        long v0 = histoAnaArgs.orderbysize ? o0.size : o0.count;
                        long v1 = histoAnaArgs.orderbysize ? o1.size : o1.count;
                        return -((v0 == v1) ? 0 : ((v0 > v1) ? 1 : -1));
                    }
                });
                if (diff.size() == 0) {
                    System.out.println("== no diff found ==");
                } else {
                    int cc = (histoAnaArgs.topcount < 0) ? Integer.MAX_VALUE : (int) histoAnaArgs.topcount;
                    cc = (cc == 0) ? 1 : cc;
                    System.out.println(StringUtil.paddingSpaceLeft("num", 6)
                                       + StringUtil.paddingSpaceLeft("#instances", 12)
                                       + StringUtil.paddingSpaceLeft("#bytes", 12)
                                       + StringUtil.paddingSpaceLeft("#human", 10)
                                       + StringUtil.paddingSpaceLeft("#aver", 7) + "  " + "class name");
                    for (int i = 0; ((i < cc) && (i < diff.size())); i++) {
                        ClassCountSize ccs = diff.get(i);
                        System.out.println(StringUtil.paddingSpaceLeft((String.valueOf(i) + ":"), 6)
                                           + StringUtil.paddingSpaceLeft(String.valueOf(ccs.count), 12)
                                           + StringUtil.paddingSpaceLeft(String.valueOf(ccs.size), 12)
                                           + StringUtil.paddingSpaceLeft(ByteUtil.formatBytes(ByteFormat.HUMAN,
                                                                                              ccs.size), 10)
                                           + StringUtil.paddingSpaceLeft(String.valueOf(ccs.size / ccs.count), 7)
                                           + "  " + ccs.name);
                    }
                }
                System.out.println();

                lastCCSM = ccsm;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Unknow error: " + ExceptionUtil.printStackTrace(e));
        } finally {
            attachTool.detach();
        }
    }

    private static String heapHisto(VirtualMachine vm, Args histoAnaArgs) {
        try {
            InputStream in = ((HotSpotVirtualMachine) vm).heapHisto((histoAnaArgs.live ? LIVE_OBJECTS_OPTION : ALL_OBJECTS_OPTION));
            try {
                return IOUtil.readToString(in, "UTF-8");
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Args parseArgs() {
        if (UnixArgsutil.ARGS.args().length == 0) {
            printUsage();
            System.exit(0);
        }
        Args args = new Args();
        String strJpid = UnixArgsutil.ARGS.args()[0];
        try {
            args.jpid = Integer.parseInt(strJpid);
        } catch (NumberFormatException nfe) {
            System.out.println("[ERROR] jpid is not number: " + strJpid);
            System.exit(0);
        }
        if (UnixArgsutil.ARGS.args().length >= 2) {
            args.interval = LongUtil.tryParse(UnixArgsutil.ARGS.args()[1], args.interval);
        }
        if (UnixArgsutil.ARGS.args().length >= 3) {
            args.count = LongUtil.tryParse(UnixArgsutil.ARGS.args()[2], args.count);
        }

        args.topcount = LongUtil.tryParse(UnixArgsutil.ARGS.kvalue("top"), args.topcount);
        args.orderbysize = !UnixArgsutil.ARGS.flags().contains("ordercount");
        args.live = UnixArgsutil.ARGS.flags().contains("live");

        return args;
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  histodiff [flags] <jpid> [<interval> [<count>]]");
        System.out.println("  java -jar histodiffall.jar [flags] <jpid> [<interval> [<count>]]");
        System.out.println("    -top <count>    Top class count[default 10, -1 unlimit]");
        System.out.println("    --ordercount    Order by count[default by size]");
        System.out.println("    --live          Dump live objects[default off]");
        System.out.println("    <jpid>          Java PID");
        System.out.println("    <interval>      Print interval(ms)[default 5000ms]");
        System.out.println("    <count>         Print count[default unlimit]");
        System.out.println("Sample:");
        System.out.println("  histodiff          -- display this message");
        System.out.println("  histodiff 12345    -- print jpid 12345's histo info");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
    }
}
