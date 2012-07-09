/*
 * Copyright (c) 2003, 2008, Oracle and/or its affiliates. All rights reserved. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES
 * OR THIS FILE HEADER. This code is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License version 2 only, as published by the Free Software Foundation. This code is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for more details (a copy is included
 * in the LICENSE file that accompanied this code). You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA 02110-1301 USA. Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA or visit
 * www.oracle.com if you need additional information or have any questions.
 */

// package sun.jvm.hotspot.tools;
// copyed from jdk source
package me.hatter.tools.permstat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.bytes.ByteUtil;
import me.hatter.tools.commons.bytes.ByteUtil.ByteFormat;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.string.StringUtil;
import sun.jvm.hotspot.memory.StringTable;
import sun.jvm.hotspot.memory.SystemDictionary;
import sun.jvm.hotspot.oops.Instance;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.OopField;
import sun.jvm.hotspot.runtime.VM;

/**
 * A command line tool to print perm. generation statistics.
 */

public class PermStat {

    private static long interval = 0L;
    private static int  count    = Integer.MAX_VALUE;

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.SA_JDI);
        UnixArgsutil.parseGlobalArgs(args);

        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }
        if (UnixArgsutil.ARGS.args().length > 1) {
            try {
                interval = Long.parseLong(UnixArgsutil.ARGS.args()[1]);
                interval = (interval < 0L) ? 0L : interval;
            } catch (Exception e) {
                LogUtil.error("Parse interval failed.");
                System.exit(-1);
            }
        }
        if (UnixArgsutil.ARGS.args().length > 2) {
            try {
                count = Integer.parseInt(UnixArgsutil.ARGS.args()[2]);
            } catch (Exception e) {
                LogUtil.error("Parse count failed.");
                System.exit(-1);
            }
        }
        //
        int loopCount = (interval == 0) ? 1 : count;
        for (int i = 0; i < loopCount; i++) {
            PrintStream err = (i == 0) ? System.out : new PrintStream(new ByteArrayOutputStream());
            PermStatTool.main(new String[] { UnixArgsutil.ARGS.args()[0] }, err);
        }
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar permstatall.jar [options] <PID> [<interval> [<count>]]");
        System.out.println("    -size B|K|M|G|H              Byte/KB/MB/GB/Humarn(default B)");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }

    public static class PermStatTool extends Tool {

        public static void main(String[] args, PrintStream err) {
            PermStatTool ps = new PermStatTool();
            ps.start(args, err);
            ps.stop();
        }

        public void run() {
            printInternStringStatistics();
        }

        protected void stop() {
            if (getAgent() != null) {
                getAgent().detach();
            }
        }

        private static int     lastCount = 0;
        private static long    lastSize  = 0L;
        private static boolean firstTime = true;

        private void printInternStringStatistics() {
            class StringStat implements StringTable.StringVisitor {

                private int      count;
                private long     size;
                private OopField stringValueField;

                StringStat() {
                    VM vm = VM.getVM();
                    SystemDictionary sysDict = vm.getSystemDictionary();
                    InstanceKlass strKlass = sysDict.getStringKlass();
                    // String has a field named 'value' of type 'char[]'.
                    stringValueField = (OopField) strKlass.findField("value", "[C");
                }

                private long stringSize(Instance instance) {
                    // We include String content in size calculation.
                    try {
                        return instance.getObjectSize() + stringValueField.getValue(instance).getObjectSize();
                    } catch (NullPointerException npe) {
                        return 0L;
                    }
                }

                public void visit(Instance str) {
                    count++;
                    size += stringSize(str);
                }

                public void print() {
                    ByteFormat format = ByteFormat.fromString(UnixArgsutil.ARGS.kvalue("size"));

                    String diffCount = (lastCount == 0) ? "-" : String.valueOf(count - lastCount);
                    String diffSize = (lastSize == 0L) ? "-" : ByteUtil.formatBytes(format, size - lastSize);
                    String diffSizeH = (lastSize == 0L) ? "-" : ByteUtil.formatBytes(ByteFormat.HUMAN, size - lastSize);

                    System.out.println(StringUtil.paddingSpaceRight(String.valueOf(count), 12)
                                       + StringUtil.paddingSpaceRight(ByteUtil.formatBytes(format, size), 18)
                                       + StringUtil.paddingSpaceRight(ByteUtil.formatBytes(ByteFormat.HUMAN, size), 14)
                                       + StringUtil.paddingSpaceRight(String.valueOf(size / count), 12)
                                       + StringUtil.paddingSpaceRight(diffCount, 12)
                                       + StringUtil.paddingSpaceRight(diffSize, 18)
                                       + StringUtil.paddingSpaceRight(diffSizeH, 18));
                    lastCount = count;
                    lastSize = size;
                }
            }

            if (firstTime) {
                firstTime = false;
                System.out.println(StringUtil.paddingSpaceRight("COUNT", 12) + StringUtil.paddingSpaceRight("SIZE", 18)
                                   + StringUtil.paddingSpaceRight("SIZE(H)", 14)
                                   + StringUtil.paddingSpaceRight("AVERAGE(B)", 12)
                                   + StringUtil.paddingSpaceRight("DIFF COUNT", 12)
                                   + StringUtil.paddingSpaceRight("DIFF SIZE", 18)
                                   + StringUtil.paddingSpaceRight("DIFF SIZE(H)", 18));
            }

            StringStat stat = new StringStat();
            StringTable strTable = VM.getVM().getStringTable();
            // VM.getVM().fireVMSuspended();
            try {
                strTable.stringsDo(stat);
            } finally {
                // VM.getVM().fireVMResumed();
            }
            stat.print();
            try {
                Thread.sleep(interval);
            } catch (Exception e) {
                // IGNORE
            }
        }
    }
}
