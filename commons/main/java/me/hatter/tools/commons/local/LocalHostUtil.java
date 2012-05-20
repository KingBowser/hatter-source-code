package me.hatter.tools.commons.local;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHostUtil {

    private static String localIp = null;
    static {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            localIp = localhost.getHostAddress();
        } catch (UnknownHostException e) {
            localIp = "unknow";
        }
    }

    public static String getLocalIp() {
        return localIp;
    }
}
