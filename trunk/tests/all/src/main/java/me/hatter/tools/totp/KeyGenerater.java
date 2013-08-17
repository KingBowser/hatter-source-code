package me.hatter.tools.totp;

public class KeyGenerater {

    public static void main(String[] args) {
        byte[] bs = new byte[20];
        for (int i = 0; i < 20; i++) {
            bs[i] = (byte) i;
        }
        System.out.println(Base32String.encode(bs));

        {
            String time = Long.toHexString(System.currentTimeMillis() / 1000 / 30 - 2);
            System.out.println(TOTP.generateTOTP(bs, time, "6", "HmacSHA1"));
        }
        {
            String time = Long.toHexString(System.currentTimeMillis() / 1000 / 30 - 1);
            System.out.println(TOTP.generateTOTP(bs, time, "6", "HmacSHA1"));
        }
        {
            String time = Long.toHexString(System.currentTimeMillis() / 1000 / 30);
            System.out.println(">> " + TOTP.generateTOTP(bs, time, "6", "HmacSHA1"));
        }
        {
            String time = Long.toHexString(System.currentTimeMillis() / 1000 / 30 + 1);
            System.out.println(TOTP.generateTOTP(bs, time, "6", "HmacSHA1"));
        }
        {
            String time = Long.toHexString(System.currentTimeMillis() / 1000 / 30 + 2);
            System.out.println(TOTP.generateTOTP(bs, time, "6", "HmacSHA1"));
        }
    }
}
