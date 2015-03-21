package me.hatter.tools.commons.security.digest;

@Deprecated
public class MD5Util {

    public static byte[] digest(byte[] bytes) {
        return Digests.md5().digest(bytes);
    }
}
