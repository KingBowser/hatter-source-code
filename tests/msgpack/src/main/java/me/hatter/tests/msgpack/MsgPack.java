package me.hatter.tests.msgpack;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.msgpack.MessagePack;

public class MsgPack {

    public static void main(String[] args) throws IOException {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("1", "aaaaaaaaaaaaaaaa");
        m.put("2", "bbbbbbbbbbbbbbbb");
        m.put("3", 1234567890);
        m.put("4", Arrays.asList("a", "b"));

        MessagePack pack = new MessagePack();
        byte[] bytes = pack.write(m);

        Object o = pack.read(bytes);
        System.out.println(o.getClass().getName());
        System.out.println(o);
    }
}
