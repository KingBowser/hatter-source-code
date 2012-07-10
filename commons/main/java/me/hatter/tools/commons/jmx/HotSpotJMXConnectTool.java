package me.hatter.tools.commons.jmx;

import java.io.File;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import me.hatter.tools.commons.jvm.HotSpotAttachTool;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;

import com.sun.tools.attach.VirtualMachine;

@SuppressWarnings("restriction")
public class HotSpotJMXConnectTool {

    public static final String CONNECTOR_ADDRESS = "com.sun.management.jmxremote.localConnectorAddress";

    private String             pid;

    public HotSpotJMXConnectTool(String pid) {
        this.pid = pid;
    }

    public JMXConnector connect() {
        String connectorAddress = getConnectorAddress();
        if (connectorAddress == null) {
            throw new RuntimeException("Cannot get connector address for pid: " + pid);
        }
        try {
            JMXServiceURL url = new JMXServiceURL(connectorAddress);
            return JMXConnectorFactory.connect(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getConnectorAddress() {
        HotSpotAttachTool attach = new HotSpotAttachTool(pid);
        attach.attach();
        try {
            VirtualMachine vm = attach.getVM();
            String connectorAddress = vm.getAgentProperties().getProperty(CONNECTOR_ADDRESS);
            if (connectorAddress == null) {
                String agent = new File(new File(vm.getSystemProperties().getProperty("java.home"), "lib"),
                                        JDKLib.MANAGEMENT_AGENT.getName()).getAbsolutePath();
                vm.loadAgent(agent);
                connectorAddress = vm.getAgentProperties().getProperty(CONNECTOR_ADDRESS);
            }
            return connectorAddress;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            attach.detach();
        }
    }
}
