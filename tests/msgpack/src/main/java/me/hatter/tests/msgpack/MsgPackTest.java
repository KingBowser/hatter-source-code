package me.hatter.tests.msgpack;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import me.hatter.tests.msgpack.obects.TestObject;
import me.hatter.tests.msgpack.obects.TestObject2;

import org.msgpack.MessagePack;

public class MsgPackTest {

    public static void main(String[] args) throws Exception {
        MessagePack pack = new MessagePack();
        pack.register(TestObject.class);
        for (int x = 0; x < 10; x++) {
            System.out.println("ROUND: " + x);
            int COUNT = 10000;
            {
                long start = System.currentTimeMillis();
                for (int i = 0; i < COUNT; i++) {
                    byte[] bs = pack.write(makeObject());
                    if (i == 0) System.out.println(bs.length);
                }
                System.out.println("COST MP: " + (System.currentTimeMillis() - start));
            }

            {
                long start = System.currentTimeMillis();
                for (int i = 0; i < COUNT; i++) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(makeObject());
                    oos.flush();
                    if (i == 0) System.out.println(baos.toByteArray().length);
                }
                System.out.println("COST S1: " + (System.currentTimeMillis() - start));
            }

            {
                long start = System.currentTimeMillis();
                for (int i = 0; i < COUNT; i++) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(makeObject2());
                    oos.flush();
                    if (i == 0) System.out.println(baos.toByteArray().length);
                }
                System.out.println("COST S2: " + (System.currentTimeMillis() - start));
            }
        }
    }

    public static TestObject makeObject() {
        TestObject to = new TestObject();
        to.setId(1);
        to.setId2(1234567890L);
        to.setId3(1.1F);
        to.setId4(1.1D);
        to.setId5("1234567890");
        return to;
    }

    public static TestObject2 makeObject2() {
        TestObject2 to = new TestObject2();
        to.setId(1);
        to.setId2(1234567890L);
        to.setId3(1.1F);
        to.setId4(1.1D);
        to.setId5("1234567890");
        return to;
    }
}
