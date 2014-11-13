package me.hatter.tools.commons.net.whois;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.whois.WhoisClient;

// http://www.nirsoft.net/whois_servers_list.html
// http://www.domaininformation.de/whoisserver_list.html
// http://www.mkyong.com/java/java-whois-example/
// http://commons.apache.org/proper/commons-net/examples/unix/fwhois.java
public class WhoisUtil {

    public static void main(String[] args) {
        // whois.pir.org
        System.out.println(queryWhois("inetsec.org"));
    }

    public static String queryWhois(String domainName) {
        StringBuilder result = new StringBuilder();
        WhoisClient whois = new WhoisClient();
        try {
            // default is internic.net
            whois.connect(WhoisClient.DEFAULT_HOST);
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
}
