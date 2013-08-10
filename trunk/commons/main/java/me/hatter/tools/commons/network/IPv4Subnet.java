package me.hatter.tools.commons.network;

/**
 * only support ipv4<br>
 * <code>192.168.0.0/32</code> OR <code>192.168.0.0:255.255.255.0</code>
 * 
 * @author hatterjiang
 */
public class IPv4Subnet {

    private int[] ip;
    private int[] mask;

    public static void main(String[] args) {
        System.out.println(new IPv4Subnet("192.168.0.0/24"));
        System.out.println(new IPv4Subnet("192.168.0.0/24").toString2());
        System.out.println(new IPv4Subnet("192.168.0.0:255.255.255.0"));
        System.out.println(new IPv4Subnet("192.168.0.0:255.255.255.0").toString2());
        System.out.println(new IPv4Subnet("192.168.0.0:255.255.255.0").matches("192.168.0.1"));
        System.out.println(new IPv4Subnet("192.168.0.0:255.255.255.0").matches("192.168.1.1"));
    }

    public IPv4Subnet(String ipmask) {
        ipmask = (ipmask == null) ? null : ipmask.trim();
        if ((ipmask == null) || ipmask.isEmpty()) {
            throw new IllegalArgumentException("ip mask is empty: " + ipmask);
        }
        int indexOfSlash = ipmask.indexOf("/");
        if (indexOfSlash > 0) {
            this.ip = IPv4Addr.ipToInts(ipmask.substring(0, indexOfSlash));
            this.mask = IPv4Mask.maskIntToIpInts(Integer.valueOf(ipmask.substring(indexOfSlash + 1)));
        }
        int indexOfColon = ipmask.indexOf(":");
        if (indexOfColon > 0) {
            this.ip = IPv4Addr.ipToInts(ipmask.substring(0, indexOfColon));
            this.mask = IPv4Addr.ipToInts(ipmask.substring(indexOfColon + 1));
        }
        if ((indexOfSlash <= 0) && (indexOfColon <= 0)) {
            throw new IllegalArgumentException("ip mask is illegal: " + ipmask);
        }
    }

    public String getIp() {
        return IPv4Addr.intsToString(ip);
    }

    public String getMask() {
        return IPv4Addr.intsToString(mask);
    }

    public int getMaskInt() {
        return IPv4Mask.ipIntsToMaskInt(mask);
    }

    public boolean matches(String targetIP) {
        int ips[] = IPv4Addr.ipToInts(targetIP);
        return IPv4Addr.intsEquals(ip, IPv4Mask.mask(ips, mask));
    }

    public String toString() {
        return IPv4Addr.intsToString(ip) + "/" + IPv4Mask.ipIntsToMaskInt(mask);
    }

    public String toString2() {
        return IPv4Addr.intsToString(ip) + ":" + IPv4Addr.intsToString(mask);
    }
}
