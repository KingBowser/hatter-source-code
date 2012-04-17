package me.hatter.tools.jtop.rmi;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.jtop.rmi.interfaces.JStackService;

public class RmiServer implements JStackService {

    private static final long serialVersionUID = -6161020091463825016L;
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

    private static boolean isStarted = false;

    synchronized public static void startup() {
        if (!isStarted) {
            try {
                (new RmiServer()).bind(1127);
            } catch (RemoteException e) {
                System.err.println("[ERROR] error in startup rmi server: " + e.getMessage());
                e.printStackTrace();
            }
            isStarted = true;
        }
    }

    public String[] listThreadInfos() throws RemoteException {
        ThreadInfo[] tis = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
        List<String> ss = new ArrayList<String>();
        // TODO Auto-generated method stub
        for (ThreadInfo ti : tis) {
            long cpu = ManagementFactory.getThreadMXBean().getThreadCpuTime(ti.getThreadId());
            ss.add(ti.getThreadId() + ": " + ti.getThreadName() + "  " + ti.getThreadState().name() + " cpu: " + cpu);
        }
        return ss.toArray(new String[0]);
    }
}
