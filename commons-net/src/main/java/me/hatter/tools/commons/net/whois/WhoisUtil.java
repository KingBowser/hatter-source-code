package me.hatter.tools.commons.net.whois;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

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
        StringBuilder result = new StringBuilder();
        WhoisClient whois = new WhoisClient();
        try {
            // default is internic.net
            whois.connect(whoisServer);
            String whoisData1 = whois.query(domainName);
            result.append(whoisData1);
            whois.disconnect();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
