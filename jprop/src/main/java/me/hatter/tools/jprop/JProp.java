package me.hatter.tools.jprop;

import java.util.Map;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.jmx.RemoteManagementTool;
import me.hatter.tools.commons.jvm.HotSpotAttachTool;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.jprop.agent.Agent;
import me.hatter.tools.jprop.management.PropertyMXBean;

public class JProp {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        UnixArgsutil.parseGlobalArgs(args);

        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }

        boolean isProperty = (!UnixArgsutil.ARGS.flags().contains("agent"));
        PropertyMXBean property = getPropertyMXBean(isProperty);

        Map<String, String> pmap = property.getPropertyMap();
        for (String key : pmap.keySet()) {
            String value = pmap.get(key);
            if (!filterKV(key, value)) continue;
            System.out.println(StringUtil.paddingSpaceRight(key, 30) + " = " + pmap.get(key));
        }
    }

    private static PropertyMXBean getPropertyMXBean(boolean isProperty) {
        String pid = UnixArgsutil.ARGS.args()[0];
        HotSpotAttachTool attach = new HotSpotAttachTool(pid);
        attach.attach();
        try {
            if (attach.getVM().getAgentProperties().get(Agent.PROPERTY_MXBEAN_PROPERTY_KEY) == null) {
                attach.getVM().loadAgent(ClassLoaderUtil.findClassJarPath(Agent.class));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            attach.detach();
        }
        RemoteManagementTool tool = new RemoteManagementTool(pid);
        String objectName = isProperty ? PropertyMXBean.SYSTEM_PROPERTY_MXBEAN_NAME : PropertyMXBean.AGENT_PROPERTY_MXBEAN_NAME;
        return tool.getManagementFactory().newPlatformMXBeanProxy(objectName, PropertyMXBean.class);
    }

    private static boolean filterKV(String key, String value) {
        if (key == null) {
            return false;
        }
        String show = UnixArgsutil.ARGS.kvalue("show");
        if ((show == null) || show.isEmpty()) {
            return true;
        }
        return key.toLowerCase().contains(show.toLowerCase());
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar jpropall.jar [options] <PID>");
        System.out.println("    <PID>                 target JVM pid");
        System.out.println("    -show <key name>      filter by key name");
        System.out.println("    --agent               show agent properties");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }
}
