# Introduction #

Introduce how does jtop work.

# Details #

Seq of jtop:

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/jtop/seq_jtop.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/jtop/seq_jtop.png)

Get RMI service client:
```
    synchronized public JStackService getJStackService() {
        try {
            if (jStackService != null) {
                return jStackService;
            }
            Registry registry = LocateRegistry.getRegistry(server, Integer.valueOf(port));
            jStackService = (JStackService) (registry.lookup("jStackService"));
            return jStackService;
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().contains("connection refused")) {
                throw new ServiceNotStartedException();
            }
            System.err.println("[ERROR] RMI register error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
```

Attach VM:
If `AttachProvider.providers().isEmpty()` then
```
    private VirtualMachine getVirtualMachineImplementationFromEmbeddedOnes() {
        try {
            if (File.separatorChar == '\\') {
                return new WindowsVirtualMachine(ATTACH_PROVIDER, pid);
            } else {
                return new LinuxVirtualMachine(ATTACH_PROVIDER, pid);
            }
        } catch (AttachNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsatisfiedLinkError ignore) {
            // noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
            throw new IllegalStateException("Unable to load Java agent; please add lib/tools.jar "
                                                          + "from your JDK to the classpath");
        }
    }
```
else
```
    private VirtualMachine attachToThisVM() {
        try {
            return VirtualMachine.attach(pid);
        } catch (AttachNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
```

Load agent:
```
    private void loadAgentAndDetachFromThisVM(VirtualMachine vm) {
        try {
            String port = System.getProperty("port", "1127");
            vm.loadAgent(jarFilePath, "port=" + port);
            vm.detach();
        } catch (AgentLoadException e) {
            throw new RuntimeException(e);
        } catch (AgentInitializationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
```

Start RMI service:
```
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
```

RMI service impl(Get server MXBean info):
```
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
```

For more, check out http://hatter-source-code.googlecode.com/svn/trunk/jtop/ and read it.

Reference:
  * http://code.google.com/p/jmockit/