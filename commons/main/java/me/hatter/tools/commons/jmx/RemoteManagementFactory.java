package me.hatter.tools.commons.jmx;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import com.sun.management.HotSpotDiagnosticMXBean;
ion")
public class RemoteManagementFactory {

    public static final String    HOT_SPOT_DIAGNOSTIC = "com.sun.management:type=HotSpotDiagnostic";

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

    public HotSpotDiagnosticMXBean getHotSpotDiagnosticMXBean() {
        return newPlatformMXBeanProxy(HOT_SPOT_DIAGNOSTIC, HotSpotDiagnosticMXBean.class);
    }

    public List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
        return newPlatformMXBeanListProxy(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE, MemoryPoolMXBean.class);
    }

    public List<MemoryManagerMXBean> getMemoryManagerMXBeans() {
        return newPlatformMXBeanListProxy(ManagementFactory.MEMORY_MANAGER_MXBEAN_DOMAIN_TYPE,
                                          MemoryManagerMXBean.class);
    }

    public List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        return newPlatformMXBeanListProxy(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE,
                                          GarbageCollectorMXBean.class);
    }

    public <T> List<T> newPlatformMXBeanListProxy(String mxbeanDomainType, Class<T> mxbeanInterface) {
        try {
            List<T> list = new ArrayList<T>();
            Set<ObjectName> objectNames = connection.queryNames(new ObjectName(mxbeanDomainType + ",*"), null);
            for (ObjectName objName : objectNames) {
                boolean emitter = connection.isInstanceOf(objName, NOTIF_EMITTER);
                T mxBean = JMX.newMXBeanProxy(connection, objName, mxbeanInterface, emitter);
                list.add(mxBean);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T newPlatformMXBeanProxy(String mxbeanName, Class<T> mxbeanInterface) {
        try {
            ObjectName objName = new ObjectName(mxbeanName);
            boolean emitter = connection.isInstanceOf(objName, NOTIF_EMITTER);
            return JMX.newMXBeanProxy(connection, objName, mxbeanInterface, emitter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final String NOTIF_EMITTER = "javax.management.NotificationEmitter";
}
