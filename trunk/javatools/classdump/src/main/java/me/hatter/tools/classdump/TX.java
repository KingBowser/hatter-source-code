package me.hatter.tools.classdump;

import java.io.PrintStream;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import sun.jvm.hotspot.runtime.JavaThread;
import sun.jvm.hotspot.runtime.Threads;
import sun.jvm.hotspot.runtime.VM;

public class TX {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.SA_JDI);
        UnixArgsutil.parseGlobalArgs(args);

        TXTool.main(new String[] { UnixArgsutil.ARGS.args()[0] }, System.out);
    }

    public static class TXTool extends Tool {

        public static void main(String[] args, PrintStream err) {
            TXTool ps = new TXTool();
            ps.start(args, err);
            ps.stop();
        }

        public void run() {
            {
                Threads ts = VM.getVM().getThreads();
                JavaThread jt = ts.first();

                for (; jt != null; jt = jt.next()) {
                    System.out.println(jt.getThreadName());
                }
            }
            {
                // Debugger debugger = getAgent().getDebugger();
                // CDebugger cdbg = debugger.getCDebugger();
                // List list = cdbg.getThreadList();
                // for (Iterator itr = list.iterator(); itr.hasNext();) {
                // ThreadProxy th = (ThreadProxy) itr.next();
                //
                // }
            }
        }
    }
}
