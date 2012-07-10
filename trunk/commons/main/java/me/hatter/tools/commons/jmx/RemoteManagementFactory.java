package me.hatter.tools.commons.jmx;

import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

import javax.management.MBeanServerConnection;

public class RemoteManagementFactory {

    private MBeanServerConnection connection;

    public RemoteManagementFactory(MBeanServerConnection connection) {
        this.connection = connection;
    }

    public ClassLoadingMXBean getClassLoadingMXBean() {
        return newPlatformMXBeanProxy(ManagementFactory.CLASS_LOADING_MXBEAN_NAME, ClassLoadingMXBean.class);
    }

    public MemoryMXBean getMemoryMXBean() {
        return newPlatformMXBeanProxy(ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
    }

    public ThreadMXBean getThreadMXBean() {
        return newPlatformMXBeanProxy(ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
    }

    public RuntimeMXBean getRuntimeMXBean() {
        return newPlatformMXBeanProxy(ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
    }

    public CompilationMXBean getCompilationMXBean() {
        return newPlatformMXBeanProxy(ManagementFactory.COMPILATION_MXBEAN_NAME, CompilationMXBean.class);
    }

    public OperatingSystemMXBean getOperatingSystemMXBean() {
        return newPlatformMXBeanProxy(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
    }

    // How to create proxy for below mxbean?
    // public static List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
    // return sun.management.ManagementFactory.getMemoryPoolMXBeans();
    // }
    //
    // public static List<MemoryManagerMXBean> getMemoryManagerMXBeans() {
    // return sun.management.ManagementFactory.getMemoryManagerMXBeans();
    // }
    //
    // public static List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
    // return sun.management.ManagementFactory.getGarbageCollectorMXBeans();
    // }

    public <T> T newPlatformMXBeanProxy(String mxbeanName, Class<T> mxbeanInterface) {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(connection, mxbeanName, mxbeanInterface);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
