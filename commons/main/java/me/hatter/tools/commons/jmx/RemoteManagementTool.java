package me.hatter.tools.commons.jmx;

import java.io.IOException;

import javax.management.remote.JMXConnector;

public class RemoteManagementTool {

    private String                  pid          = null;
    private HotSpotJMXConnectTool   connectTool  = null;
    private JMXConnector            jmxConnector = null;
    private RemoteManagementFactory factory      = null;

    public RemoteManagementTool(String pid) {
        this.pid = pid;
        this.connectTool = new HotSpotJMXConnectTool(this.pid);
    }

    synchronized public RemoteManagementFactory getManagementFactory() {
        if (factory == null) {
            try {
                factory = new RemoteManagementFactory(getJmxConnector().getMBeanServerConnection());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return factory;
    }

    synchronized public JMXConnector getJmxConnector() {
        if (jmxConnector == null) {
            jmxConnector = connectTool.connect();
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    close();
                }
            });
        }
        return jmxConnector;
    }

    synchronized public void close() {
        if (jmxConnector != null) {
            try {
                jmxConnector.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            jmxConnector = null;
        }
    }
}
