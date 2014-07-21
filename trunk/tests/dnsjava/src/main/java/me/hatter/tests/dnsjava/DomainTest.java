package me.hatter.tests.dnsjava;

import java.io.IOException;
import java.net.InetAddress;

import me.hatter.tools.commons.string.StringUtil;

import org.xbill.DNS.Address;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class DomainTest {

    public static void main(String[] args) throws IOException {
        String[] domains = new String[]{"hongsengroup.com", "lizhong-hardware.com", "mx.hongsengroup.com",
                "mx.zhuguang.com.cn", "www.hongsengroup.com", "www.zhuguang.com.cn", "zhuguang.com.cn"};
        for (String domain : domains) {
            System.out.println(domain + "::");
            try {
                InetAddress addr = Address.getByName(domain);
                System.out.println(" --> " + addr.getHostAddress());
            } catch (Exception e) {
                System.out.println(" --> " + e.getMessage());
            }

            System.out.println(".. " + "NS");
            lookup(domain, Type.NS);
            System.out.println(".. " + "A");
            lookup(domain, Type.A);
            System.out.println(".. " + "CNAME");
            lookup(domain, Type.CNAME);
            System.out.println(".. " + "MX");
            lookup(domain, Type.MX);
            System.out.println(StringUtil.repeat("=", 80));
        }
    }

    public static void lookup(String domain, int type) throws IOException {
        Lookup lookup = new Lookup(domain, type);
        Record[] records = lookup.run();
        if (records == null) {
            System.out.println("---- NO RECORDS ----");
        } else {
            for (Record r : records) {
                System.out.println(r);
            }
        }
    }
}
