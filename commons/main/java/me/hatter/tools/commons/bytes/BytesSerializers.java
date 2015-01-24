package me.hatter.tools.commons.bytes;

import me.hatter.tools.commons.bytes.impl.Base64ByteSerializer;
import me.hatter.tools.commons.bytes.impl.HexByteSerializer;

public class BytesSerializers {

    public static BytesSerializer base64() {
        return new Base64ByteSerializer();
    }

    public static BytesSerializer hex() {
        return new HexByteSerializer();
    }
}
