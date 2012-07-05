package me.hatter.tools.jtop.agent;

import java.io.IOException;
import java.util.Properties;

import me.hatter.tools.commons.jvm.HotSpotAttachTool;
import me.hatter.tools.jtop.util.EnvUtil;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.VirtualMachine;

public class JDK6AgentLoader {

    private final String jarFilePath;
    private final String pid;

    public JDK6AgentLoader(String jarFilePath, String pid) {
        this.jarFilePath = jarFilePath;
        this.pid = pid;
    }

    public String loadAgent() {
        HotSpotAttachTool attach = new HotSpotAttachTool(pid);
        attach.attach();
        try {
            loadAgentAndDetachFromThisVM(attach.getVM());
            return attach.getVM().getSystemProperties().getProperty("jtop.port");
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            String port = String.valueOf(EnvUtil.getPort());
            vm.loadAgent(jarFilePath, "port=" + port);
        } catch (AgentLoadException e) {
            throw new RuntimeException(e);
        } catch (AgentInitializationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
