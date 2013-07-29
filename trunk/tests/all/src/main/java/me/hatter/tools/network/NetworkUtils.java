package me.hatter.tools.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkUtils {

    public static void main(String[] args) throws Exception {
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("::: " + local);
        Enumeration<NetworkInterface> nie = NetworkInterface.getNetworkInterfaces();
        while (nie.hasMoreElements()) {
            NetworkInterface ni = nie.nextElement();
            System.out.println(ni + ": " + ni.getInterfaceAddresses());
        }
    }
}
