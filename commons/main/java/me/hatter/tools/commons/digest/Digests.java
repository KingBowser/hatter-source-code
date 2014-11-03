package me.hatter.tools.commons.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Enumeration;

public class Digests {

    public static interface Digest {

        byte[] digest(byte[] bytes);
    }

    public static void main(String[] args) {
        {
            Provider provider[] = Security.getProviders();
            for (Provider pro : provider) {
                System.out.println(pro);
                for (Enumeration<?> e = pro.keys(); e.hasMoreElements();)
                    System.out.println("\t" + e.nextElement());

            }
        }

        System.out.println("-------------------------------------------------------------------------------");

        {
            System.out.println("Algorithms Supported in this JCE.");
            System.out.println("====================");
            // heading
            System.out.println("Provider: type.algorithm -> className" + "\n  aliases:" + "\n  attributes:\n");
            // discover providers
            Provider[] providers = Security.getProviders();
            for (Provider provider : providers) {
                System.out.println("<><><>" + provider + "<><><>\n");
                // discover services of each provider
                for (Provider.Service service : provider.getServices()) {
                    System.out.println(service);
                }
                System.out.println();
            }
        }
    }

    @Deprecated
    public static Digest md2() {
        return getDigest("MD2");
    }

    public static Digest md5() {
        return getDigest("MD5");
    }

    public static Digest sha1() {
        return getDigest("SHA1");
    }

    public static Digest sha256() {
        return getDigest("SHA-256");
    }

    public static Digest sha384() {
        return getDigest("SHA-384");
    }

    public static Digest sha512() {
        return getDigest("SHA-512");
    }

    public static Digest getDigest(final String d) {
        return new Digest() {

            @Override
            public byte[] digest(byte[] bytes) {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance(d);
                    messageDigest.reset();
                    messageDigest.update(bytes);
                    byte[] bs = messageDigest.digest();
                    return bs;
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
