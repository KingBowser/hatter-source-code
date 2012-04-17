package me.hatter.tools.jtop.agent;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.spi.AttachProvider;

@SuppressWarnings("restriction")
public class JDK6AgentLoader {

    private final String jarFilePath;
    private final String pid;

    public JDK6AgentLoader(String jarFilePath) {
        this.jarFilePath = jarFilePath;
        pid = discoverProcessIdForRunningVM();
    }

    public JDK6AgentLoader(String jarFilePath, String pid) {
        this.jarFilePath = jarFilePath;
        this.pid = pid;
    }

    public String discoverProcessIdForRunningVM() {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int p = nameOfRunningVM.indexOf('@');
        return nameOfRunningVM.substring(0, p);
    }

    public void loadAgent() {
        VirtualMachine vm;
        if (AttachProvider.providers().isEmpty()) {
            throw new IllegalStateException("Failed AttachProvider.providers().isEmpty().");
        } else {
            vm = attachToThisVM();
        }
        loadAgentAndDetachFromThisVM(vm);
    }

    private VirtualMachine attachToThisVM() {
        try {
            return VirtualMachine.attach(pid);
        } catch (AttachNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAgentAndDetachFromThisVM(VirtualMachine vm) {
        try {
            vm.loadAgent(jarFilePath, null);
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
