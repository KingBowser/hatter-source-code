package me.hatter.tools.commons.network;

/**
 * only support ipv4<br>
 * <code>192.168.0.0/32</code> OR <code>192.168.0.0:255.255.255.0</code>
 * 
 * @author hatterjiang
 */
public class SubnetMask {

    private int[] ip;
    private int[] mask;

    public static void main(String[] args) {
        System.out.println(new SubnetMask("192.168.0.0/24"));
        System.out.println(new SubnetMask("192.168.0.0/24").toString2());
        System.out.println(new SubnetMask("192.168.0.0:255.255.255.0"));
        System.out.println(new SubnetMask("192.168.0.0:255.255.255.0").toString2());
        System.out.println(new SubnetMask("192.168.0.0:255.255.255.0").matches("192.168.0.1"));
        System.out.println(new SubnetMask("192.168.0.0:255.255.255.0").matches("192.168.1.1"));
    }

    public SubnetMask(String ipmask) {
        ipmask = (ipmask == null) ? null : ipmask.trim();
        if ((ipmask == null) || ipmask.isEmpty()) {
            throw new IllegalArgumentException("ip mask is empty: " + ipmask);
        }
        int indexOfSlash = ipmask.indexOf("/");
        if (indexOfSlash > 0) {
            this.ip = ipToIpInts(ipmask.substring(0, indexOfSlash));
            this.mask = maskIntToIpInts(Integer.valueOf(ipmask.substring(indexOfSlash + 1)));
        }
        int indexOfColon = ipmask.indexOf(":");
        if (indexOfColon > 0) {
            this.ip = ipToIpInts(ipmask.substring(0, indexOfColon));
            this.mask = ipToIpInts(ipmask.substring(indexOfColon + 1));
        }
        if ((indexOfSlash <= 0) && (indexOfColon <= 0)) {
            throw new IllegalArgumentException("ip mask is illegal: " + ipmask);
        }
    }

    public String getIp() {
        return ipIntsToString(ip);
    }

    public String getMask() {
        return ipIntsToString(mask);
    }

    public int getMaskInt() {
        return ipIntsToMaskInt(mask);
    }

    public boolean matches(String targetIP) {
        int ips[] = ipToIpInts(targetIP);
        return equals(ip, mask(ips, mask));
    }

    public String toString() {
        return ipIntsToString(ip) + "/" + ipIntsToMaskInt(mask);
    }

    public String toString2() {
        return ipIntsToString(ip) + ":" + ipIntsToString(mask);
    }

    private static String ipIntsToString(int[] ips) {
        return String.valueOf(ips[0]) + "." + String.valueOf(ips[1]) + "." + String.valueOf(ips[2]) + "."
               + String.valueOf(ips[3]);
    }

    private static boolean equals(int[] ip1, int[] ip2) {
        for (int i = 0; i < 4; i++) {
            if (ip1[i] != ip2[i]) {
                return false;
            }
        }
        return true;
    }

    private static int[] mask(int[] ip, int[] mask) {
        int[] masked = new int[] { 0, 0, 0, 0 };
        for (int i = 0; i < 4; i++) {
            masked[i] = ip[i] & mask[i];
        }
        return masked;
    }

    private static int ipIntsToMaskInt(int[] ips) {
        int maskInt = 0;
        for (int i = 0; i < 4; i++) {
            int ip = ips[i];
            for (int j = 0; j < 8; j++) {
                if ((ip & 0x80) != 0) {
                    maskInt++;
                } else {
                    return maskInt;
                }
                ip <<= 1;
            }
        }
        return maskInt;
    }

    private static int[] maskIntToIpInts(int maskint) {
        if ((maskint < 0) || (maskint > 32)) {
            throw new IllegalArgumentException("mask int is illegal: " + maskint);
        }
        int[] ips = new int[] { 0, 0, 0, 0 };
        for (int i = 0; i < 32; i++) {
            if (i < maskint) {
                ips[i / 8] <<= 1;
                ips[i / 8] |= 1;
            } else {
                ips[i / 8] <<= 1;
            }
        }
        return ips;
    }

    private static int[] ipToIpInts(String ip) {
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
}
