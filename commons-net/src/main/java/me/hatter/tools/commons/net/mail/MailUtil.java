package me.hatter.tools.commons.net.mail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.mail.Header;
import javax.mail.internet.MimeMessage;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.function.Function;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.net.mail.header.Mail;
import me.hatter.tools.commons.net.mail.header.Received;
import me.hatter.tools.commons.string.StringUtil;

public class MailUtil {

    public static Mail parseMail(byte[] bytes) {
        return parseMailAndClose(new ByteArrayInputStream(bytes));
    }

    public static Mail parseMailAndClose(InputStream is) {
        try {
            return parseMail(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.closeQuietly(is);
        }
    }

    public static Mail parseMail(InputStream is) throws Exception {
        MimeMessage mm = new MimeMessage(null, is);
        @SuppressWarnings("unchecked")
        List<Header> headers = CollectionUtil.it(mm.getAllHeaders()).asList();

        Mail m = new Mail();
        m.setFrom(getValue(getHeader(headers, "From")));
        m.setSender(getValue(getHeader(headers, "Sender")));
        m.setReturnPath(getValue(getHeader(headers, "Return-Path")));
        m.setReplay(getValue(getHeader(headers, "Reply-To")));
        m.setTo(getValue(getHeader(headers, "To")));
        m.setMailer(getValue(getHeader(headers, "Mailer", "X-Mailer")));
        m.setMessageId(getValue(getHeader(headers, "Message-ID")));
        m.setSubject(getValue(getHeader(headers, "Subject")));
        m.setSenderIp(getValue(getHeader(headers, "X-Originating-IP")));
        m.setAuthenticationResults(getValue(getHeader(headers, "Authentication-Results")));

        List<Header> receivedList = getHeaders(headers, "Received", "X-Received");
        List<Received> rl = CollectionUtil.it(receivedList).map(new Function<Header, Received>() {

            @Override
            public Received apply(Header obj) {
                return ReceivedParser.parseReceived(obj.getValue());
            }
        }).asList();
        m.setReceivedList(rl);
        return m;
    }

    public static String getValue(Header header) {
        return ((header == null) ? null : header.getValue());
    }

    public static Header getHeader(List<Header> headers, String... headerNames) {
        return CollectionUtil.firstObject(getHeaders(headers, headerNames));
    }

    public static List<Header> getHeaders(List<Header> headers, String... headerNames) {
        List<Header> hs = new ArrayList<Header>();
        for (Header h : headers) {
            for (String hn : headerNames) {
                if (hn.equalsIgnoreCase(h.getName().trim())) {
                    hs.add(h);
                }
            }
        }
        return hs;
    }

    public static boolean isMailEquals(String m0, String m1) {
        m0 = StringUtil.trim(parseMailAddress(m0));
        m1 = StringUtil.trim(parseMailAddress(m1));
        return StringUtil.equalsIgnoreCase(m0, m1);
    }

    public static String parseMailAddress(String m) {
        if (m == null) {
            return null;
        }
        if (m.contains("<")) {
            return StringUtil.substringBeforeLast(StringUtil.substringAfterLast(m, "<"), ">");
        }
        return m;
    }

    public static String parseMailDomain(String m) {
        if (m == null) {
            return null;
        }
        m = parseMailAddress(m);
        return m.contains("@") ? StringUtil.substringAfter(m, "@") : m;
    }

    public static String formatDateCST(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm:ss Z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }
}
