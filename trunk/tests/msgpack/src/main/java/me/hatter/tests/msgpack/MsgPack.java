package me.hatter.tests.msgpack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.msgpack.MessagePack;

public class MsgPack {

    public static void main(String[] args) throws IOException {
        Map<String, String> m = new HashMap<String, String>();
        m.put("1", "aaaaaaaaaaaaaaaa");
        m.put("2", "bbbbbbbbbbbbbbbb");

        MessagePack pack = new MessagePack();
        byte[] bytes = pack.write(m);

        Object o = pack.read(bytes);
        System.out.println(o);
    }
}
