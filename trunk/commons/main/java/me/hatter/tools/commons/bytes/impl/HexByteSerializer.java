package me.hatter.tools.commons.bytes.impl;

import java.io.ByteArrayOutputStream;

import me.hatter.tools.commons.bytes.BytesSerializer;

public class HexByteSerializer implements BytesSerializer {

    @Override
    public String serializer(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return bytes2String(bytes);
    }

    @Override
    public byte[] deSerializer(String str) {
        if (str == null) {
            return null;
        }
        return string2Bytes(str);
    }

    private static String bytes2String(byte[] bytes) {
        StringBuilder string = new StringBuilder();
        for (byte b : bytes) {
            String hexString = Integer.toHexString(0x00FF & b);
            string.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return string.toString();
    }

    private static byte[] string2Bytes(String str) {
        int len = str.length();
        if ((len % 2) != 0) {
            throw new RuntimeException("String format error: " + str);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < len; i++) {
            String substr = new String(new char[] { str.charAt(i++), str.charAt(i) });
            baos.write((byte) Integer.parseInt(substr, 16));
        }
        return baos.toByteArray();
    }
}
