package me.hatter.tools.classlist;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.bytecode.ByteCodeUtil;
import me.hatter.tools.commons.bytes.ByteUtil;
import me.hatter.tools.commons.bytes.ByteUtil.ByteFormat;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.string.StringUtil;
import sun.jvm.hotspot.memory.SystemDictionary;
import sun.jvm.hotspot.memory.SystemDictionary.ClassVisitor;
import sun.jvm.hotspot.oops.Klass;

public class ClassList {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.SA_JDI);
        UnixArgsutil.parseGlobalArgs(args);

        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }

        ClassListTool.main(new String[] { UnixArgsutil.ARGS.args()[0] }, System.out);
    }

    public static class ClassListTool extends Tool {

        public static void main(String[] args, PrintStream err) {
            ClassListTool ps = new ClassListTool();
            ps.start(args, err);
            ps.stop();
        }

        public void run() {
            runClassListTool();
        }

        private static void runClassListTool() {
            final AtomicLong totalCount = new AtomicLong(0);
            final AtomicLong totalSize = new AtomicLong(0);
            final AtomicLong filterCount = new AtomicLong(0);
            final AtomicLong filterSize = new AtomicLong(0);

            final ConcurrentMap<String, AtomicLong> packageCountMap = new ConcurrentHashMap<String, AtomicLong>();
            final ConcurrentMap<String, AtomicLong> packageSizeMap = new ConcurrentHashMap<String, AtomicLong>();

            final boolean detail = UnixArgsutil.ARGS.flags().contains("detail");
            final int npackage = Integer.parseInt(UnixArgsutil.ARGS.kvalue("grouping"));

            SystemDictionary sysDict = new SystemDictionary();

            if (detail) {
                System.out.println(StringUtil.paddingSpaceRight("Class Name", 60) + " Size");
                System.out.println(StringUtil.repeat("-", 70));
            }

            sysDict.allClassesDo(new ClassVisitor() {

                public void visit(Klass klass) {
                    totalCount.incrementAndGet();
                    totalSize.addAndGet(klass.getObjectSize());

                    AtomicBoolean isPrimary = new AtomicBoolean(false);
                    String fullClassName = ByteCodeUtil.resolveClassName(klass.getName().asString(), false, isPrimary);
                    String className = ByteCodeUtil.resolveClassName(klass.getName().asString(),
                                                                     UnixArgsutil.ARGS.flags().contains("short"), null);

                    boolean isMatch = true;
                    if (UnixArgsutil.ARGS.keys().contains("show")) {
                        String _show = UnixArgsutil.ARGS.kvalue("show").toLowerCase();
                        if (_show.startsWith("^")) {
                            isMatch = fullClassName.toLowerCase().startsWith(_show.substring(1));
                        } else {
                            isMatch = fullClassName.toLowerCase().contains(_show);
                        }
                    }

                    if (isMatch) {
                        filterCount.incrementAndGet();
                        filterSize.addAndGet(klass.getObjectSize());
                        if (detail) {
                            System.out.println(StringUtil.paddingSpaceRight(className, 60) + " "
                                               + klass.getObjectSize());
                        }
                        // ---------------------------------------------------------------------
                        if (!isPrimary.get()) {
                            String npackageName = getNPackageName(fullClassName, npackage);
                            AtomicLong count = packageCountMap.get(npackageName);
                            if (count == null) {
                                packageCountMap.putIfAbsent(npackageName, new AtomicLong(0));
                                count = packageCountMap.get(npackageName);
                            }
                            count.incrementAndGet();
                            AtomicLong size = packageSizeMap.get(npackageName);
                            if (size == null) {
                                packageSizeMap.putIfAbsent(npackageName, new AtomicLong(0));
                                size = packageSizeMap.get(npackageName);
                            }
                            size.addAndGet(klass.getObjectSize());
                        }
                    }
                }
            });
            if (detail) {
                System.out.println(StringUtil.repeat("-", 70));
            }
            System.out.println("Total class count: " + totalCount.get());
            System.out.println("Total class size: " + totalSize.get() + " (H: "
                               + ByteUtil.formatBytes(ByteFormat.HUMAN, totalSize.get()) + ")");
            System.out.println("Average total class size: " + (totalSize.get() / totalCount.get()));
            if (UnixArgsutil.ARGS.keys().contains("show")) {
                System.out.println("==== Filter with: " + UnixArgsutil.ARGS.kvalue("show") + " ====");
                if (filterCount.get() == 0) {
                    System.out.println("Filter result is empty.");
                } else {
                    System.out.println("Filter class count: " + filterCount.get());
                    System.out.println("Filter class size: " + filterSize.get() + " (H: "
                                       + ByteUtil.formatBytes(ByteFormat.HUMAN, filterSize.get()) + ")");
                    System.out.println("Average filter class size: " + (filterSize.get() / filterCount.get()));
                }
            }
            if (UnixArgsutil.ARGS.keys().contains("grouping")) {
                System.out.println("==== Grouping depth: " + UnixArgsutil.ARGS.kvalue("grouping") + " ====");
                System.out.println(StringUtil.paddingSpaceRight("Grouping Package", 35) + " "
                                   + StringUtil.paddingSpaceRight("Count", 10)
                                   + StringUtil.paddingSpaceRight("Size", 12) + " Human(size)");
                System.out.println(StringUtil.repeat("-", 70));
                for (String npackageName : packageCountMap.keySet()) {
                    System.out.println(StringUtil.paddingSpaceRight(npackageName, 35)
                                       + " "
                                       + StringUtil.paddingSpaceRight(String.valueOf(packageCountMap.get(npackageName).longValue()),
                                                                      10)
                                       + " "
                                       + StringUtil.paddingSpaceRight(String.valueOf(packageSizeMap.get(npackageName).longValue()),
                                                                      12)
                                       + " "
                                       + ByteUtil.formatBytes(ByteFormat.HUMAN,
                                                              packageSizeMap.get(npackageName).longValue()));
                }
            }
        }

        private static String getNPackageName(String className, int np) {
            if (className == null) {
                return null;
            }
            List<String> cnl = new ArrayList<String>();
            String[] cns = className.split("\\.");
            for (int i = 0; ((i < cns.length) && (i < np)); i++) {
                cnl.add(cns[i]);
            }
            return StringUtil.join(cnl, ".");
        }

        protected void stop() {
            if (getAgent() != null) {
                getAgent().detach();
            }
        }
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar classlistall.jar [options] <PID>");
        System.out.println("    -show <class name>       filter by full class name");
        System.out.println("    -grouping <firt n>       group class first n package");
        System.out.println("    --detail                 print detail");
        System.out.println("    --short                  print short class name");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }
}
