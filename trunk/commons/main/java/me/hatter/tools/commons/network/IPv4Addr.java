package me.hatter.tools.commons.network;

public class IPv4Addr {

    private int[] ip;

    public IPv4Addr(String ip) {
        this.ip = ipToInts(ip);
    }

    public int[] getInts() {
        int[] copiedIp = new int[ip.length];
        for (int i = 0; i < ip.length; i++) {
            copiedIp[i] = ip[i];
        }
        return copiedIp;
    }

    public String toString() {
        return intsToString(this.ip);
    }

    public static boolean intsEquals(int[] ip1, int[] ip2) {
        if ((ip1 == null) && (ip2 == null)) {
            return true;
        }
        if ((ip1 == null) || (ip2 == null)) {
            return false;
        }
        if (ip1.length != ip2.length) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (ip1[i] != ip2[i]) {
                return false;
            }
        }
        return true;
    }

    public static int[] ipToInts(String ip) {
        if (ip == null) {
            throw new IllegalArgumentException("ip is null");
        }
        String[] subs = ip.split("\\.");
        if (subs.length != 4) {
            throw new IllegalArgumentException("ip is illegal: " + ip);
        }
        int[] ips = new int[4];
        for (int i = 0; i < 4; i++) {
            try {
                ips[i] = Integer.parseInt(subs[i]);
                if ((ips[i] < 0) || (ips[i] > 255)) {
                    throw new IllegalArgumentException("ip is illegal: " + ip);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("ip is illegal: " + ip);
            }
        }
        return ips;
    }

    public static String intsToString(int[] ips) {
        return String.valueOf(ips[0]) + "." + String.valueOf(ips[1]) + "." + String.valueOf(ips[2]) + "."
               + String.valueOf(ips[3]);
    }
}
