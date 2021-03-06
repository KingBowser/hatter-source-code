package me.hatter.tools.hostsmanager;

import java.util.Arrays;

import me.hatter.tools.resourceproxy.commons.util.ArgsUtil;
import me.hatter.tools.resourceproxy.jsspserver.handler.HttpServerHandler;
import me.hatter.tools.resourceproxy.jsspserver.main.MainHttpServer;

public class HostsManager {

    public static void main(String[] args) {
        args = ArgsUtil.processArgs(args);
        int port = Integer.parseInt(System.getProperty("port", "1127"));
        MainHttpServer httpServer = new MainHttpServer(new HttpServerHandler(), Arrays.asList(port));
        httpServer.run();
    }
}
