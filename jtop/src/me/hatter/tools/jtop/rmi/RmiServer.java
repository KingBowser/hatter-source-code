package me.hatter.tools.jtop.rmi;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadInfo;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.jtop.agent.Agent;
import me.hatter.tools.jtop.rmi.interfaces.JClassLoadingInfo;
import me.hatter.tools.jtop.rmi.interfaces.JGCInfo;
import me.hatter.tools.jtop.rmi.interfaces.JMemoryInfo;
import me.hatter.tools.jtop.rmi.interfaces.JMemoryUsage;
import me.hatter.tools.jtop.rmi.interfaces.JStackService;
import me.hatter.tools.jtop.rmi.interfaces.JThreadInfo;

public class RmiServer implements JStackService {

    private static final long serialVersionUID = -6161020091463825016L;

    private static RmiServer  RMI_SERVER       = null;

    private int               thisPort;
    private String            thisAddress;
    private Registry          registry;

    public void receiveMessage(String x) throws RemoteException {
        System.out.println(x);
    }

    public RmiServer() throws RemoteException {
        try {
            thisAddress = (InetAddress.getLocalHost()).toString();
        } catch (Exception e) {
            throw new RemoteException("can't get inet address.");
        }
    }

    public void bind(int port) throws RemoteException {
        this.thisPort = port;
        System.out.println("[INFO] this address=" + thisAddress + ",port=" + thisPort);
        try {
            registry = LocateRegistry.createRegistry(thisPort);
            registry.rebind("jStackService", (JStackService) UnicastRemoteObject.exportObject(this, 0));
        } catch (RemoteException e) {
            throw e;
        }
    }

    synchronized public static void startup(int port) {
        if (RMI_SERVER == null) {
            try {
                RMI_SERVER = new RmiServer();
                RMI_SERVER.bind(port);
                System.setProperty("jtop.port", String.valueOf(port));
            } catch (RemoteException e) {
                System.err.println("[ERROR] error in startup rmi server: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // implemention
    public String getProcessId() throws RemoteException {
        return Agent.discoverProcessIdForRunningVM();
    }

    public JMemoryInfo getMemoryInfo() throws RemoteException {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return new JMemoryInfo(new JMemoryUsage(memoryMXBean.getHeapMemoryUsage()),
                               new JMemoryUsage(memoryMXBean.getNonHeapMemoryUsage()));
    }

    public JGCInfo[] getGCInfos() throws RemoteException {
        List<JGCInfo> jgcInfos = new ArrayList<JGCInfo>();
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            JGCInfo jgcInfo = new JGCInfo();
            jgcInfo.setName(garbageCollectorMXBean.getName());
            jgcInfo.setValid(garbageCollectorMXBean.isValid());
            jgcInfo.setMemoryPoolNames(garbageCollectorMXBean.getMemoryPoolNames());
            jgcInfo.setCollectionCount(garbageCollectorMXBean.getCollectionCount());
            jgcInfo.setCollectionTime(garbageCollectorMXBean.getCollectionTime());
            jgcInfos.add(jgcInfo);
        }
        return jgcInfos.toArray(new JGCInfo[0]);
    }

    public JClassLoadingInfo getClassLoadingInfo() throws RemoteException {
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        return new JClassLoadingInfo(classLoadingMXBean.getTotalLoadedClassCount(),
                                     classLoadingMXBean.getLoadedClassCount(),
                                     classLoadingMXBean.getUnloadedClassCount());
    }

    public JThreadInfo[] listThreadInfos() throws RemoteException {
        ThreadInfo[] tis = ManagementFactory.getThreadMXBean().dumpAllThreads(false, false);
        JThreadInfo[] jtis = new JThreadInfo[tis.length];
        for (int i = 0; i < tis.length; i++) {
            long threadId = tis[i].getThreadId();
            long cpuTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(threadId);
            long userTime = ManagementFactory.getThreadMXBean().getThreadUserTime(threadId);
            jtis[i] = new JThreadInfo(tis[i], cpuTime, userTime);
        }
        return jtis;
    }
}
