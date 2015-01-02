package me.hatter.tests.https;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;

import javax.crypto.Cipher;

import me.hatter.tools.commons.misc.Base64;

// http://stackoverflow.com/questions/521101/using-sha1-and-rsa-with-java-security-signature-vs-messagedigest-and-cipher
// http://docs.oracle.com/javase/tutorial/security/
// http://docs.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html
// http://docs.oracle.com/javase/6/docs/technotes/guides/security/SunProviders.html
// http://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html
// http://docs.oracle.com/javase/6/docs/technotes/guides/security/p11guide.html
public class Sign {

    public static void main(String[] args) throws Exception {
        // Generate new key
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        String plaintext = "This is the message being signed";

        // Compute signature
        Signature instance = Signature.getInstance("SHA1withRSA");
        instance.initSign(privateKey);
        instance.update((plaintext).getBytes());
        byte[] signature = instance.sign();

        // Compute digest
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        byte[] digest = sha1.digest((plaintext).getBytes());

        // Encrypt digest
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherText = cipher.doFinal(digest);

        // Display results
        System.out.println("Input data: " + plaintext);
        System.out.println("Digest: " + bytes2String(digest));
        System.out.println("Cipher text: " + bytes2String(cipherText));
        System.out.println("Signature: " + bytes2String(signature));
        System.out.println("Signature: " + Base64.byteArrayToBase64(signature));
    }

    private static String bytes2String(byte[] bytes) {
        StringBuilder string = new StringBuilder();
        for (byte b : bytes) {
            String hexString = Integer.toHexString(0x00FF & b);
            string.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return string.toString();
    }
}
