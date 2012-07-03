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

import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import sun.jvm.hotspot.memory.StringTable;
import sun.jvm.hotspot.memory.SystemDictionary;
import sun.jvm.hotspot.oops.Instance;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.OopField;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;

/**
 * A command line tool to print perm. generation statistics.
 */

public class PermStat {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.SA_JDI);
        PermStatTool.main(args);
    }

    public static class PermStatTool extends Tool {

        public static void main(String[] args) {
            PermStatTool ps = new PermStatTool();
            ps.start(args);
            ps.stop();
        }

        public void run() {
            printInternStringStatistics();
        }

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
                    System.out.println(count + " intern Strings occupying " + size + " bytes.");
                }
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
        }
    }
}
