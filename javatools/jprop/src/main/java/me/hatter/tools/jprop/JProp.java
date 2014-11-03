package me.hatter.tools.jprop;

import java.util.List;
import java.util.Map;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jmx.CustomJMXConnectTool;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.log.LogUtil;
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

        if (UnixArgsutil.ARGS.keys().contains("set") || UnixArgsutil.ARGS.keys().contains("remove")) {
            {
                List<String> setList = UnixArgsutil.ARGS.kvalues("set");
                if ((setList != null) && (!setList.isEmpty())) {
                    for (String set : setList) {
                        if (!set.contains("=")) {
                            LogUtil.error("Format error: " + set);
                        } else {
                            String key = StringUtil.substringBefore(set, "=");
                            String value = StringUtil.substringAfter(set, "=");
                            LogUtil.info("Set property: " + key + " = " + value + " @"
                                         + (isProperty ? "system" : "agent"));
                            property.setPropery(key, value);
                        }
                    }
                }
            }

            {
                List<String> removeList = UnixArgsutil.ARGS.kvalues("remove");
                if ((removeList != null) && (!removeList.isEmpty())) {
                    for (String remove : removeList) {
                        LogUtil.info("Remove property: " + remove + " @" + (isProperty ? "system" : "agent"));
                        property.clearPropery(remove);
                    }
                }
            }
        } else {
            Map<String, String> pmap = property.getPropertyMap();
            for (String key : pmap.keySet()) {
                String value = pmap.get(key);
                if (!filterKV(key, value)) continue;
                System.out.println(StringUtil.paddingSpaceRight(key, 30) + " = " + pmap.get(key));
            }
        }
    }

    private static PropertyMXBean getPropertyMXBean(boolean isProperty) {
        String pid = UnixArgsutil.ARGS.args()[0];
        CustomJMXConnectTool tool = new CustomJMXConnectTool(pid, Agent.PROPERTY_MXBEAN_PROPERTY_KEY, Agent.class);
        String objectName = isProperty ? PropertyMXBean.SYSTEM_PROPERTY_MXBEAN_NAME : PropertyMXBean.AGENT_PROPERTY_MXBEAN_NAME;
        return tool.getCustomMXBean(PropertyMXBean.class, objectName);
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
        System.out.println("    -set <key>=<vlue>     set key & value");
        System.out.println("    -remove <key>         remove key");
        System.out.println("    --agent               show agent properties");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }
}
