package me.hatter.tests.localdnscache;

import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.hatter.tools.commons.string.StringUtil;

public class LocalDnsCacheTest {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10000; i++) {
            System.out.println("IP of www.alibaba.com is @"
                               + new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()) + ": "
                               + bytesToIP(Inet4Address.getByName("www.alibaba.com").getAddress()));
            Thread.sleep(TimeUnit.MINUTES.toMillis(1));
        }
    }

    public static String bytesToIP(byte[] bytes) {
        List<String> ips = new ArrayList<String>();
        for (byte b : bytes) {
            ips.add(String.valueOf(((int) b) & 0xFF));
        }
        return StringUtil.join(ips, ".");
    }
}
