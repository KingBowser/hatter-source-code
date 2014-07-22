package me.hatter.tests.dnsjava;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;

import org.xbill.DNS.Address;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class NSTool {
    private static final LogTool log = LogTools.getLogTool(NSTool.class);

    private String domain;
    private Map<String, Integer> typeMap;

    public NSTool(String domain, String[] types) {
        this.domain = domain;
        this.typeMap = new LinkedHashMap<String, Integer>();
        for (String type : types) {
            if ("IP".equalsIgnoreCase(type)) {
                typeMap.put(type, -1);
            } else {
                try {
                    typeMap.put(type, getTypeValue(type));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new NSTool("alibaba.com", new String[]{"IP", "NS", "A", "MX"}).lookup());
    }

    public Map<String, Object> lookup() {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        for (Entry<String, Integer> typeEntry : typeMap.entrySet()) {
            log.info("Looking up: " + domain + ", TYPE: " + typeEntry.getKey());
            try {
                if ("IP".equalsIgnoreCase(typeEntry.getKey())) {
                    InetAddress addr = Address.getByName(domain);
                    resultMap.put(typeEntry.getKey(), addr.getHostAddress());
                } else {
                    Lookup lookup = new Lookup(domain, typeEntry.getValue().intValue());
                    Record[] records = lookup.run();
                    resultMap.put(typeEntry.getKey(),
                            ((records == null) ? null : new ArrayList<Record>(Arrays.asList(records))));
                }
            } catch (Exception e) {
                resultMap.put(typeEntry.getKey(), "ERROR: " + e.getMessage());
            }
        }
        return resultMap;
    }

    private static int getTypeValue(String type) throws Exception {
        Field f = Type.class.getDeclaredField(type);
        return (Integer) f.get(null);
    }
}
