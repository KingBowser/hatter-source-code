package me.hatter.tools.resourceproxy.jsspserver.main;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MainHttpServer implements Runnable {

    private static final LogTool logTool = LogTools.getLogTool(MainHttpServer.class);

    private List<Integer>        ports;
    private HttpHandler          httpHandler;

    public MainHttpServer(HttpHandler httpHandler, List<Integer> ports) {
        this.httpHandler = httpHandler;
        this.ports = ports;
    }

    public void run() {
        long start = System.currentTimeMillis();
        List<Integer> successPorts = new ArrayList<Integer>();
        for (int port : ports) {
            try {
                InetSocketAddress addr = new InetSocketAddress(port);
                HttpServer httpServer = HttpServer.create(addr, 0);
                int httpserverThreads = Integer.valueOf(System.getProperty("httpserver.threads", "4"));
                httpServer.setExecutor(Executors.newFixedThreadPool(httpserverThreads));
                httpServer.createContext("/", httpHandler);
                httpServer.start();
                successPorts.add(port);
            } catch (BindException be) {
                logTool.error("Bind port failed: " + port);
            } catch (IOException e) {
                logTool.error("Exception occured: " + StringUtil.printStackTrace(e));
            }
        }
        if (logTool.isInfoEnable()) {
            logTool.info("Start HttpServer on: " + successPorts + " cost: " + (System.currentTimeMillis() - start)
                         + " ms");
        }
    }
}
