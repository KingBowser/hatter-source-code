package me.hatter.tools.iapp.main;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.Arrays;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;

import me.hatter.tools.commons.args.PICArgs;
import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jmx.HotSpotJMXConnectTool;
import me.hatter.tools.commons.jmx.RemoteManagementFactory;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;

public class Main {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        UnixArgsutil.parseGlobalArgs(args);
        PICArgs picArgs = new PICArgs();
        picArgs.parseArgs(UnixArgsutil.ARGS.args());
        if (!picArgs.isPidSetted()) {
            usage();
        }

        HotSpotJMXConnectTool jmxConnect = new HotSpotJMXConnectTool(String.valueOf(picArgs.getPid()));
        JMXConnector jmxConnector = jmxConnect.connect();
        try {
            MBeanServerConnection conn = jmxConnector.getMBeanServerConnection();

            RemoteManagementFactory factory = new RemoteManagementFactory(conn);

            List<MemoryPoolMXBean> poolList = factory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean pool : poolList) {
                System.out.println("" + pool.getName() + ": " + pool.getType());
                System.out.println("    " + Arrays.asList(pool.getMemoryManagerNames()));
                System.out.println("    " + pool.getUsage());
                System.out.println("    " + pool.getPeakUsage());
                System.out.println("    " + pool.getCollectionUsage());
                // System.out.println("    " + pool.getUsageThreshold() + " - " + pool.getUsageThresholdCount());
                // System.out.println("    " + pool.getCollectionUsageThreshold() + " - "
                // + pool.getCollectionUsageThresholdCount());
            }

            System.out.println("=============================================================================");
            List<MemoryManagerMXBean> managerList = factory.getMemoryManagerMXBeans();
            for (MemoryManagerMXBean manager : managerList) {
                System.out.println("" + manager.getName() + " : " + Arrays.asList(manager.getMemoryPoolNames()));
            }
            System.out.println("=============================================================================");
            List<GarbageCollectorMXBean> collectorList = factory.getGarbageCollectorMXBeans();
            for (GarbageCollectorMXBean collector : collectorList) {
                System.out.println("" + collector.getName() + " : " + Arrays.asList(collector.getMemoryPoolNames()));
                System.out.println("    " + collector.getCollectionTime() + "  -  " + collector.getCollectionCount());
            }
            System.out.println("=============================================================================");
            MemoryMXBean memory = factory.getMemoryMXBean();
            System.out.println(memory.getHeapMemoryUsage());
            System.out.println(memory.getNonHeapMemoryUsage());
            System.out.println(memory.getObjectPendingFinalizationCount());

            // ClassLoadingMXBean classLoader = ManagementFactory.newPlatformMXBeanProxy(conn,
            // ManagementFactory.CLASS_LOADING_MXBEAN_NAME,
            // ClassLoadingMXBean.class);
            // System.out.println("Loaded class: " + classLoader.getLoadedClassCount());
            // System.out.println("Total loaded class: " + classLoader.getTotalLoadedClassCount());
            // System.out.println("Is verbose: " + classLoader.isVerbose());
            jmxConnector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println("<pid> [<interval> [<count>]]");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }
}
