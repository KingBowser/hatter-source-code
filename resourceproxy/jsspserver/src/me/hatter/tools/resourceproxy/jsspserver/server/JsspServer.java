package me.hatter.tools.resourceproxy.jsspserver.server;

import java.io.File;

import com.sun.net.httpserver.HttpExchange;

public class JsspServer {

    private static File JSSP_PATH;
    static {
        String jsspPath = System.getProperty("jssp.path");
        if (jsspPath == null) {
            JSSP_PATH = new File(System.getProperty("user.dir"), "jssp");
        } else {
            JSSP_PATH = new File(jsspPath);
        }
    }

    @SuppressWarnings("restriction")
    public static void process(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();

        File jsspFile = new File(JSSP_PATH, path);
    }
}
