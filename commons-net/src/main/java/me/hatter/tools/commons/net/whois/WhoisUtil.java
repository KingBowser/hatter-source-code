package me.hatter.tools.commons.net.whois;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.function.Filter;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.StringUtil;

import org.apache.commons.net.whois.WhoisClient;

// http://www.nirsoft.net/whois_servers_list.html
// http://www.domaininformation.de/whoisserver_list.html
// http://www.mkyong.com/java/java-whois-example/
// http://commons.apache.org/proper/commons-net/examples/unix/fwhois.java
public class WhoisUtil {

    private static final LogTool  log             = LogTools.getLogTool(WhoisUtil.class);
    private static List<String[]> domainWhoisList = new ArrayList<String[]>();
    static {
        initWhoisServerList();
    }

    public static String whois(String domainName) {
        boolean recursion = false;
        if (domainName.toLowerCase().endsWith(".com") || domainName.toLowerCase().endsWith(".net")) {
            recursion = true;
        }
        return whois(domainName, recursion);
    }

    public static String whois(String domainName, boolean recursion) {
        List<String> ws = queryWhois(domainName, recursion);
        return StringUtil.join(ws, StringUtil.repeat(Environment.LINE_SEPARATOR, 3));
    }

    public static List<String> queryWhois(String domainName, boolean recursion) {
        List<String> result = new ArrayList<String>();
        String q1 = queryWhois(domainName);
        result.add(q1);

        if (recursion && (q1 != null)) {
            String[] lns = q1.split("(\r\n)|(\r)|(\n)");
            List<String> res = CollectionUtil.it(Arrays.asList(lns)).filter(new Filter<String>() {

                @Override
                public boolean accept(String obj) {
                    return obj.trim().toLowerCase().startsWith("whois server:");
                }
            }).asList();
            if (!res.isEmpty()) {
                String parsedWhoisSever = StringUtil.trim(StringUtil.substringAfter(res.get(0), ":"));
                if (StringUtil.isNotBlank(parsedWhoisSever)) {
                    result.add(queryWhois(domainName, parsedWhoisSever));
                }
            }
        }
        return result;
    }

    public static String queryWhois(String domainName) {
        if (domainName == null) {
            return null;
        }
        domainName = domainName.trim().toLowerCase();
        if (domainName.endsWith(".")) {
            domainName = domainName.substring(0, domainName.length() - 1);
        }
        for (String[] dw : domainWhoisList) {
            if (domainName.endsWith("." + dw[0])) {
                return queryWhois(domainName, dw[1]);
            }
        }
        return queryWhois(domainName, WhoisClient.DEFAULT_HOST);
    }

    public static String queryWhois(String domainName, String whoisServer) {
        if (log.isInfoEnable()) {
            log.info("Query '" + domainName + "' from '" + whoisServer + "'.");
        }
        StringBuilder result = new StringBuilder();
        WhoisClient whois = new WhoisClient();
        try {
            whois.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(10));
            // default is internic.net
            whois.connect(whoisServer);
            whois.setSoTimeout((int) TimeUnit.SECONDS.toMillis(60));
            String whoisData1 = whois.query(domainName);
            result.append(whoisData1);
            whois.disconnect();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }

    private static void initWhoisServerList() {
        InputStream is = WhoisUtil.class.getResourceAsStream("/whois-servers.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            for (String l; ((l = br.readLine()) != null);) {
                if (StringUtil.isBlank(l)) {
                    continue;
                }
                if (l.trim().startsWith(";")) {
                    continue;
                }
                l = l.trim().toLowerCase();
                String[] la = l.split("\\s+");
                if (la.length == 2) {
                    domainWhoisList.add(la);
                } else {
                    log.error("Error line: " + l);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.closeQuietly(br);
            IOUtil.closeQuietly(is);
        }
    }
}
