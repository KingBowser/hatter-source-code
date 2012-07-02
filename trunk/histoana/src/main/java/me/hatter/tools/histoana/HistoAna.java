package me.hatter.tools.histoana;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.number.LongUtil;
import me.hatter.tools.commons.os.OSUtil;
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
        UnixArgsutil.parseGlobalArgs(args);
        final Args histoAnaArgs = parseArgs();
        tryAddLibToolsJar();

        // File jmap = new File(System.getProperty("java.home"), "/bin/jmap");
        // System.out.println(jmap.exists());
        // System.out.println(jmap.getAbsolutePath());

        VirtualMachine vm = attach(String.valueOf(histoAnaArgs.jpid));
        final AtomicReference<VirtualMachine> refVM = new AtomicReference<VirtualMachine>(vm);
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                detach(refVM);
            }
        });
        try {
            ClassCountSizeMap lastCCSM = HistoParser.parseHisto(heapHisto(refVM.get(), histoAnaArgs));
            for (long loop = 0; loop < histoAnaArgs.count; loop++) {
                Thread.sleep((histoAnaArgs.interval < 0) ? 0 : histoAnaArgs.interval);
                ClassCountSizeMap ccsm = HistoParser.parseHisto(heapHisto(refVM.get(), histoAnaArgs));

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
                    System.out.println(StringUtil.paddingSpaceLeft("num", 8)
                                       + StringUtil.paddingSpaceLeft("#instances", 15)
                                       + StringUtil.paddingSpaceLeft("#bytes", 15) + "  " + "class name");
                    for (int i = 0; ((i < cc) && (i < diff.size())); i++) {
                        ClassCountSize ccs = diff.get(i);
                        System.out.println(StringUtil.paddingSpaceLeft((String.valueOf(i) + ":"), 8)
                                           + StringUtil.paddingSpaceLeft(String.valueOf(ccs.count), 15)
                                           + StringUtil.paddingSpaceLeft(String.valueOf(ccs.size), 15) + "  "
                                           + ccs.name);
                    }
                }
                System.out.println();

                lastCCSM = ccsm;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Unknow error: " + ExceptionUtil.printStackTrace(e));
        } finally {
            detach(refVM);
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

    private static void detach(AtomicReference<VirtualMachine> refVM) {
        synchronized (refVM) {
            if (refVM.get() != null) {
                VirtualMachine vm = refVM.get();
                refVM.set(null);
                try {
                    System.out.println("[INFO] Detach from vm.");
                    vm.detach();
                } catch (IOException e) {
                    LogUtil.error("Detach from vm failed: " + ExceptionUtil.printStackTrace(e));
                }
            }
        }
    }

    private static VirtualMachine attach(String pid) {
        try {
            System.out.println("[INFO] Attach to vm: " + pid);
            return VirtualMachine.attach(pid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void tryAddLibToolsJar() {
        if (OSUtil.isUnixCompatible() && (!OSUtil.isMacOS())) {
            File toolsJar = new File(Environment.JAVA_HOME, "lib/tools.jar").getAbsoluteFile();
            if (!toolsJar.exists()) {
                LogUtil.error("JDK tools.jar not found: " + toolsJar.getPath());
                return;
            }
            try {
                System.out.println("[INFO] Add system classloader jar url: " + toolsJar);
                ClassLoaderUtil.addURLs(ClassLoaderUtil.getSystemURLClassLoader(), toolsJar.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
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
    }
}
