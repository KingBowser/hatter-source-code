package me.hatter.tests.dnsjava;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class DnsJavaTest {

    public static void main(String[] args) throws Exception {
        Lookup lookup = new Lookup("outlook.com", Type.A);
        Record[] records = lookup.run();
        for (Record r : records) {
            System.out.println(r);
        }
    }
}
