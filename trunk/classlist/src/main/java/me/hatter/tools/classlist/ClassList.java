package me.hatter.tools.classlist;

import java.io.PrintStream;

import sun.jvm.hotspot.memory.SystemDictionary;
import sun.jvm.hotspot.memory.SystemDictionary.ClassVisitor;
import sun.jvm.hotspot.oops.Klass;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.permstat.Tool;

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

    private static void runClassListTool() {

        SystemDictionary sysDict = new SystemDictionary();
        sysDict.allClassesDo(new ClassVisitor() {

            public void visit(Klass arg0) {
                // System.out.println(arg0);
                System.out.println(arg0.getName().asString());
                System.out.println("    " + arg0.getObjectSize());
                System.out.println("    " + arg0.getClassStatus());
                System.out.println("    " + arg0.getClassModifiers());
            }
        });
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

        protected void stop() {
            if (getAgent() != null) {
                getAgent().detach();
            }
        }
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar classlistall.jar [options] <PID>");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }
}
