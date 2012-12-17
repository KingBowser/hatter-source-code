package me.hatter.tests.jdktest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class TransferToTest {

    public static void main(String[] args) throws IOException {
        String f = "/Users/hatterjiang/Desktop/ae_product_business_info_20121014_H1B.dat";
        FileInputStream fis = new FileInputStream(f);
        FileOutputStream fos = new FileOutputStream("/Users/hatterjiang/Desktop/out.out");

        long timeStar = System.currentTimeMillis();
        FileChannel channel = fis.getChannel();
        WritableByteChannel channel2 = Channels.newChannel(fos);
        channel.transferTo(0, new File(f).length(), channel2);
        channel2.close();
        fos.close();
        fis.close();
        long timeEnd = System.currentTimeMillis();
        System.out.println("Total time :" + (timeEnd - timeStar) + "ms");
    }
}
