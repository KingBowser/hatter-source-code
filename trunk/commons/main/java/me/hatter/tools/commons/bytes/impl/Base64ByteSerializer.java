package me.hatter.tools.commons.bytes.impl;

import me.hatter.tools.commons.bytes.BytesSerializer;
import me.hatter.tools.commons.misc.Base64;

public class Base64ByteSerializer implements BytesSerializer {

    @Override
    public String serializer(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.byteArrayToBase64(bytes);
    }

    @Override
    public byte[] deSerializer(String str) {
        if (str == null) {
            return null;
        }
        return Base64.base64ToByteArray(str);
    }

}
