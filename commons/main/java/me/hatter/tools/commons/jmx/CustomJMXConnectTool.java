package me.hatter.tools.commons.jmx;

import java.util.HashSet;
import java.util.Set;

import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.jvm.HotSpotAttachTool;

public class CustomJMXConnectTool {

    private String                   pid;
    private String                   agentInitKey;
    private String                   agentJarFile;
    private RemoteManagementTool     tool;

    private static final Set<String> agentInitKeySet = new HashSet<String>();

    public CustomJMXConnectTool(String pid, String agentInitKey, Class<?> clazz) {
        this(pid, agentInitKey, ClassLoaderUtil.findClassJarPath(clazz));
    }

    public CustomJMXConnectTool(String pid, String agentInitKey, String agentJarFile) {
        this.pid = pid;
        this.agentInitKey = agentInitKey;
        this.agentJarFile = agentJarFile;
    }

    synchronized RemoteManagementFactory getRemoteManagementFactory() {
        if (tool == null) {
            tool = new RemoteManagementTool(pid);
        }
        return tool.getManagementFactory();
    }

    @SuppressWarnings("restriction")
    synchronized public <T> T getCustomMXBean(Class<T> clazzMXBean, String objectName) {
        if (!agentInitKeySet.contains(agentInitKey)) {
            HotSpotAttachTool attach = new HotSpotAttachTool(pid);
            attach.attach();
            try {
                if (attach.getVM().getAgentProperties().get(agentInitKey) == null) {
                    attach.getVM().loadAgent(agentJarFile);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                attach.detach();
            }
            agentInitKeySet.add(agentInitKey);
        }
        return getRemoteManagementFactory().newPlatformMXBeanProxy(objectName, clazzMXBean);
    }
}
