package me.hatter.tools.jtop.agent;

import java.io.IOException;
import java.util.Properties;

import me.hatter.tools.commons.jvm.HotSpotAttachTool;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.VirtualMachine;

public class JDK6AgentLoader {

    private final String jarFilePath;
    private final String pid;

    public JDK6AgentLoader(String jarFilePath) {
        this.jarFilePath = jarFilePath;
        pid = Agent.discoverProcessIdForRunningVM();
    }

    public JDK6AgentLoader(String jarFilePath, String pid) {
        this.jarFilePath = jarFilePath;
        this.pid = pid;
    }

    public void loadAgent() {
        HotSpotAttachTool attach = new HotSpotAttachTool(pid);
        attach.attach();
        try {
            loadAgentAndDetachFromThisVM(attach.getVM());
        } finally {
            attach.detach();
        }
    }

    public String getVMProperty(String key) {
        HotSpotAttachTool attach = new HotSpotAttachTool(pid);
        attach.attach();
        try {
            Properties properties = attach.getVM().getSystemProperties();
            String value = properties.getProperty(key);
            return value;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            attach.detach();
        }
    }

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
}
