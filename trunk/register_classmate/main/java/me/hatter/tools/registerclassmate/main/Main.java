package me.hatter.tools.registerclassmate.main;

import java.util.Arrays;

import me.hatter.tools.resourceproxy.commons.util.ArgsUtil;
import me.hatter.tools.resourceproxy.dbutils.config.PropertyConfig;
import me.hatter.tools.resourceproxy.dbutils.dataaccess.DataAccessObject;
import me.hatter.tools.resourceproxy.jsspserver.handler.HttpServerHandler;
import me.hatter.tools.resourceproxy.jsspserver.main.MainHttpServer;

public class Main {

    public static final DataAccessObject _DAO = new DataAccessObject(PropertyConfig.createSqliteConfig("classmate.db"));

    public static void main(String[] args) {
        args = ArgsUtil.processArgs(args);
        int port = Integer.parseInt(System.getProperty("port", "1234"));
        MainHttpServer httpServer = new MainHttpServer(new HttpServerHandler(), Arrays.asList(port));
        httpServer.run();
    }
}
