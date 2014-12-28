package me.hatter.tools.commons.network;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

public class Proxys {

    public static Proxy socks(String host, int port) {
        return new Proxy(Type.SOCKS, new InetSocketAddress(host, port));
    }

    public static Proxy http(String host, int port) {
        return new Proxy(Type.HTTP, new InetSocketAddress(host, port));
    }
}
