package me.hatter.tools.commons.misc;

import java.io.IOException;
import java.net.InetSocketAddress;

import me.hatter.tools.commons.log.LogUtil;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class RunOnceByNetworkPort {

    public static void checkRunning(int port) {
        checkRunning(port, true);
    }

    public static boolean checkRunning(int port, boolean autoExitVM) {
        try {
            LogUtil.info("Binding network: " + port);
            InetSocketAddress addr = new InetSocketAddress(port);
            HttpServer httpServer = HttpServer.create(addr, 0);
            httpServer.createContext("/", new HttpHandler() {

                public void handle(HttpExchange exchange) throws IOException {
                    LogUtil.info("On HTTP: " + exchange);
                }
            });
            httpServer.start();
            return true;
        } catch (IOException e) {
            LogUtil.warn("Already started!");
            if (autoExitVM) {
                System.exit(0);
            }
            return false;
        }
    }
}
