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
        System.out.println(new IPv4Subnet("192.168.0.0/24").toString3());
        System.out.println(new IPv4Subnet("192.168.1.0/16").toString());
        System.out.println(new IPv4Subnet("192.168.1.0/16").toString2());
        System.out.println(new IPv4Subnet("192.168.1.0/16").toString3());
        System.out.println(new IPv4Subnet("192.168.1.0/24").toString());
        System.out.println(new IPv4Subnet("192.168.1.0/24").toString2());
        System.out.println(new IPv4Subnet("192.168.1.0/24").toString3());
        System.out.println(new IPv4Subnet("192.168.0.0:255.255.255.0"));
        System.out.println(new IPv4Subnet("192.168.0.0:255.255.255.0").toString2());
        System.out.println(new IPv4Subnet("192.168.0.0:255.255.255.0").matches("192.168.0.1"));
        System.out.println(new IPv4Subnet("192.168.0.0:255.255.255.0").matches("192.168.1.1"));
    }

    public IPv4Subnet(String ip, int mask) {
        this.ip = IPv4Addr.ipToInts(ip);
        this.mask = IPv4Mask.maskIntToIpInts(mask);
        resolveIpMask();
    }

    public IPv4Subnet(IPv4Addr ip, int mask) {
        this.ip = ip.getInts();
        this.mask = IPv4Mask.maskIntToIpInts(mask);
        resolveIpMask();
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
            resolveIpMask();
        }
        int indexOfColon = ipmask.indexOf(":");
        if (indexOfColon > 0) {
            this.ip = IPv4Addr.ipToInts(ipmask.substring(0, indexOfColon));
            this.mask = IPv4Addr.ipToInts(ipmask.substring(indexOfColon + 1));
            resolveIpMask();
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

    public boolean matches(IPv4Addr targetIP) {
        return IPv4Addr.intsEquals(ip, IPv4Mask.mask(targetIP.getInts(), mask));
    }

    public int hashCode() {
        int hashCode = 1;
        if (ip != null) {
            for (int i = 0; i < ip.length; i++) {
                hashCode = 31 * hashCode + ip[i];
            }
        }
        if (mask != null) {
            for (int i = 0; i < mask.length; i++) {
                hashCode = 31 * hashCode + mask[i];
            }
        }
        return hashCode;
    }

    public boolean equals(Object object) {
        if ((object == null) || (object.getClass() != IPv4Subnet.class)) {
            return false;
        }
        IPv4Subnet another = (IPv4Subnet) object;
        return (IPv4Addr.intsEquals(this.ip, another.ip) && IPv4Addr.intsEquals(this.mask, another.mask));
    }

    public String toString() {
        return IPv4Addr.intsToString(ip) + "/" + IPv4Mask.ipIntsToMaskInt(mask);
    }

    public String toString(String split) {
        return IPv4Addr.intsToString(ip) + split + IPv4Addr.intsToString(mask);
    }

    public String toString2() {
        return toString(":");
    }

    public String toString3() {
        return toString(" - ");
    }

    private void resolveIpMask() {
        if ((ip != null) && (mask != null) && (ip.length == mask.length)) {
            for (int i = 0; i < ip.length; i++) {
                ip[i] = ip[i] & mask[i];
            }
        }
    }
}
