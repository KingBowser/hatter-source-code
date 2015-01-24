package me.hatter.tools.commons.bytes;

public interface BytesSerializer {

    String serializer(byte[] bytes);
    
    byte[] deSerializer(String str);
}
