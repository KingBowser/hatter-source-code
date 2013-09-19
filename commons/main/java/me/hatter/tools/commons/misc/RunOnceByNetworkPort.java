package me.hatter.tools.commons.misc;

import java.io.IOException;
import java.net.InetSocketAddress;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class RunOnceByNetworkPort {

    private static final LogTool logTool = LogTools.getLogTool(RunOnceByNetworkPort.class);

    public static void checkRunning(int port) {
        checkRunning(port, true);
    }

    public static boolean checkRunning(int port, boolean autoExitVM) {
        try {
            logTool.info("Binding network: " + port);
            InetSocketAddress addr = new InetSocketAddress(port);
            HttpServer httpServer = HttpServer.create(addr, 0);
            httpServer.createContext("/", new HttpHandler() {

                public void handle(HttpExchange exchange) throws IOException {
                    logTool.info("On HTTP: " + exchange);
                }
            });
            httpServer.start();
            return true;
        } catch (IOException e) {
            if (autoExitVM) {
                logTool.error("Already started!");
                System.exit(0);
            } else {
                logTool.warn("Already started!");
            }
            return false;
        }
    }
}
