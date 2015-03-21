package me.hatter.tools.commons.security.hmac;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMacs {

    public static interface Hmac {

        byte[] sign(byte[] bytes);
    }

    public static Hmac sha1(byte[] key) {
        return getHMac(key, "HmacSHA1");
    }

    public static Hmac sha256(byte[] key) {
        return getHMac(key, "HmacSHA256");
    }

    public static Hmac sha512(byte[] key) {
        return getHMac(key, "HmacSHA512");
    }

    public static Hmac getHMac(final byte[] key, final String d) {
        return new Hmac() {

            @Override
            public byte[] sign(byte[] bytes) {
                try {
                    Mac mac = Mac.getInstance(d);
                    SecretKeySpec secretKey = new SecretKeySpec(key, d);
                    mac.init(secretKey);
                    return mac.doFinal(bytes);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
