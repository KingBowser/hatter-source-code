package me.hatter.tools.jtop.agent;

import java.io.File;
import java.io.IOException;

import sun.tools.attach.LinuxVirtualMachine;
import sun.tools.attach.WindowsVirtualMachine;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.spi.AttachProvider;

public class JDK6AgentLoader {

    private static final AttachProvider ATTACH_PROVIDER = new AttachProvider() {

                                                            @Override
                                                            public String name() {
                                                                return null;
                                                            }

                                                            @Override
                                                            public String type() {
                                                                return null;
                                                            }

                                                            @Override
                                                            public VirtualMachine attachVirtualMachine(String id) {
                                                                return null;
                                                            }
                                                        };

    private final String                jarFilePath;
    private final String                pid;

    public JDK6AgentLoader(String jarFilePath) {
        this.jarFilePath = jarFilePath;
        pid = Agent.discoverProcessIdForRunningVM();
    }

    public JDK6AgentLoader(String jarFilePath, String pid) {
        this.jarFilePath = jarFilePath;
        this.pid = pid;
    }

    public void loadAgent() {
        VirtualMachine vm;
        if (AttachProvider.providers().isEmpty()) {
            vm = getVirtualMachineImplementationFromEmbeddedOnes();
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
            throw new IllegalStateException(
                                            "Unable to load Java agent; please add lib/tools.jar from your JDK to the classpath");
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
