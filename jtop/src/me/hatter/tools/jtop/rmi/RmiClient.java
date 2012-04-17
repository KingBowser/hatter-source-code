package me.hatter.tools.jtop.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

import me.hatter.tools.jtop.rmi.interfaces.JStackService;

public class RmiClient {

    private String        server;
    private int           port;
    private JStackService jStackService;

    public RmiClient(String server, int port) {
        this.server = server;
        this.port = port;
    }

    synchronized public JStackService getJStackService() {
        try {
            if (jStackService != null) {
                return jStackService;
            }
            Registry registry = LocateRegistry.getRegistry(server, Integer.valueOf(port));
            Object o = registry.lookup("jStackService");
            System.out.println("XXX" + Arrays.asList(o.getClass().getInterfaces()));
            jStackService = (JStackService) (registry.lookup("jStackService"));
            return jStackService;
        } catch (Exception e) {
            System.err.println("[ERROR] RMI register error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
