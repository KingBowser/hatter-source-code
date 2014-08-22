package me.hatter.tests.https;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.string.StringUtil;

// http://stackoverflow.com/questions/20601336/java-https-no-verify-certificate
public class HttpsTest3 {

    public static void main(String[] args) throws IOException, Exception {
        URL url1 = new URL("https://hatter.me/");

        if (url1.getProtocol().equalsIgnoreCase("https")) {// you dont need this check
            HostnameVerifier hv = new HostnameVerifier() {

                public boolean verify(String urlHostName, javax.net.ssl.SSLSession session) {

                    if (urlHostName.equals(session.getPeerHost())) {
                        System.out.println("Verified HTTPS " + session.getPeerHost() + "  >> " + urlHostName);
                    } else {
                        System.out.println("Warning: URL host " + urlHostName + " is different to SSLSession host "
                                           + session.getPeerHost());
                    }
                    return true;
                }
            };

            TrustManager[] trustAll = new TrustManager[] { new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {

                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    if (certs != null) {
                        System.out.println("checkClientTrusted");
                        int i = 0;
                        for (X509Certificate cert : certs) {
                            System.out.println(i + "\t" + StringUtil.repeat("=", 150));
                            System.out.println(cert);
                            i++;
                        }
                    }
                    System.out.println(authType);
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    if (certs != null) {
                        System.out.println("checkServerTrusted");
                        int i = 0;
                        for (X509Certificate cert : certs) {
                            System.out.println(i + "\t" + StringUtil.repeat("=", 150));
                            System.out.println(cert);
                            i++;
                        }
                    }
                    System.out.println(authType);

                }
            } };

            SSLContext sc = SSLContext.getInstance("SSL"); // TLS ?

            sc.init(null, trustAll, new SecureRandom());

            SSLSocketFactory factory = (SSLSocketFactory) sc.getSocketFactory();
            // HttpsURLConnection.setDefaultSSLSocketFactory(factory);
            // HttpsURLConnection.setDefaultHostnameVerifier(hv);

            HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();
            connection.setHostnameVerifier(hv);
            connection.setSSLSocketFactory(factory);

            // connection.setDoOutput(true);
            // connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);

            InputStream is = connection.getInputStream();
            System.out.println(IOUtil.readToString(is));
            IOUtil.closeQuietly(is);
        }
    }
}
