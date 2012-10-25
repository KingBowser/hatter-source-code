package me.hatter.tools.sa;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.sa.tests.*;

public class Test {

    public static void main(String[] args) throws Exception {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.SA_JDI);
        UnixArgsutil.parseGlobalArgs(args);

        TestAllObjects.main(UnixArgsutil.ARGS.args(), System.out);
//        sun.jvm.hotspot.tools.soql.SOQL.main(UnixArgsutil.ARGS.args());
    }
}
