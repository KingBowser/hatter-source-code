package me.hatter.tools.commons.network;

public class IPv4Mask {

    public static int[] mask(int[] ip, int[] mask) {
        int[] masked = new int[] { 0, 0, 0, 0 };
        for (int i = 0; i < 4; i++) {
            masked[i] = ip[i] & mask[i];
        }
        return masked;
    }

    public static int ipIntsToMaskInt(int[] ips) {
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

    public static int[] maskIntToIpInts(int maskint) {
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
}
