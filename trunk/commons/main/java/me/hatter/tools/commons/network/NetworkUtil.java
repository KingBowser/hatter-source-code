package me.hatter.tools.commons.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtil {

    public static String getLocalIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
