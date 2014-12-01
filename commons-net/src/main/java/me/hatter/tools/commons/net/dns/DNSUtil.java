package me.hatter.tools.commons.net.dns;

import java.io.IOException;
import java.net.UnknownHostException;

import me.hatter.tools.commons.collection.Cu;
import me.hatter.tools.commons.function.Function;
import me.hatter.tools.commons.string.StringUtil;

import org.xbill.DNS.DClass;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;
import org.xbill.DNS.SimpleResolver;

public class DNSUtil {

    public static String joinRecords(Record[] records) {
        return joinRecords(records, "^^^");
    }

    public static String joinRecords(Record[] records, final String separater) {
        if ((records == null) || (records.length == 0)) {
            return null;
        }
        return Cu.it(records).map(new Function<Record, String>() {

            @Override
            @SuppressWarnings("unchecked")
            public String apply(Record r) {
                if (r instanceof org.xbill.DNS.TXTRecord) {
                    return Cu.it(((org.xbill.DNS.TXTRecord) r).getStrings()).join(separater);
                } else if (r instanceof org.xbill.DNS.MXRecord) {
                    return ((org.xbill.DNS.MXRecord) r).getTarget().toString();
                } else if (r instanceof org.xbill.DNS.CNAMERecord) {
                    return ((org.xbill.DNS.CNAMERecord) r).getTarget().toString();
                } else {
                    return ((org.xbill.DNS.ARecord) r).getAddress().getHostAddress();
                }
            }
        }).join(separater);
    }

    public static Record[] queryNS(String domain, SimpleResolver res, int v) throws IOException {
        Name name = Name.fromString(domain);
        Record rec = Record.newRecord(name, v, DClass.IN);
        Message query = Message.newQuery(rec);
        Message resp = res.send(query);
        Record[] records = resp.getSectionArray(Section.ANSWER);
        return records;
    }

    public static SimpleResolver getResolver(String ns) {
        try {
            return StringUtil.isEmpty(ns) ? new SimpleResolver() : new SimpleResolver(ns);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
