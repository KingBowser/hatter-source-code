package me.hatter.tests.jdktest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferTest {

    // http://www.zhurouyoudu.com/index.php/archives/470/
    public static void main(String[] args) throws IOException {
        ByteBuffer byteBuf = ByteBuffer.allocate(1024 * 14 * 1024);
        byte[] bbb = new byte[14 * 1024 * 1024];
        FileInputStream fis = new FileInputStream("/Users/hatterjiang/Desktop/ae_product_business_info_20121014_H1B.dat");
        FileOutputStream fos = new FileOutputStream("/Users/hatterjiang/Desktop/out.out");
        FileChannel fc = fis.getChannel();
        long timeStar = System.currentTimeMillis();// 得到当前的时间
        fc.read(byteBuf);// 1 读取
        // MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        System.out.println(fc.size() / 1024);
        long timeEnd = System.currentTimeMillis();// 得到当前的时间
        System.out.println("Read time :" + (timeEnd - timeStar) + "ms");
        timeStar = System.currentTimeMillis();
        fos.write(bbb);// 2.写入
        // mbb.flip();
        timeEnd = System.currentTimeMillis();
        System.out.println("Write time :" + (timeEnd - timeStar) + "ms");
        fos.flush();
        fc.close();
        fis.close();
    }
}
