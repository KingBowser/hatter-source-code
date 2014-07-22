package me.hatter.tests.dnsjava;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.hatter.tools.commons.collection.CollectionUtil.Group;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;

public class DomainTest {

    public static void main(String[] args) throws IOException {
        String[] domains = new String[]{"hongsengroup.com", "lizhong-hardware.com", "mx.hongsengroup.com",
                "mx.zhuguang.com.cn", "www.hongsengroup.com", "www.zhuguang.com.cn", "zhuguang.com.cn"};
        Group<String> table = new Group<String>();
        table.add("<table>");
        for (String domain : domains) {
            NSTool nsTool = new NSTool(domain, new String[]{"IP", "NS", "A", "CNAME", "MX"});
            Map<String, Object> result = nsTool.lookup();
        }
        table.add("</table>");
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
