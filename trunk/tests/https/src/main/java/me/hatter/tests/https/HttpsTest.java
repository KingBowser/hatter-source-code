package me.hatter.tests.https;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import me.hatter.tools.commons.io.IOUtil;

//  openssl s_client -connect google.com:443 | openssl x509 -text
// https://developer.android.com/training/articles/security-ssl.html#CommonProblems
public class HttpsTest {

    public static void main(String[] args) throws IOException {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                System.out.println("::: " + hostname);
                System.out.println("::: " + session.getPeerHost());
                System.out.println("::: " + session.getPeerPort());
                System.out.println("::: " + Arrays.asList(session.getValueNames()));
                return hv.verify("github.com", session);
            }
        };
        URL url = new URL("https://github.com");
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setHostnameVerifier(hostnameVerifier);
        InputStream in = urlConnection.getInputStream();
        System.out.println("::: " + urlConnection.getPeerPrincipal());
        IOUtil.copy(in, System.out);
        IOUtil.closeQuietly(in);
    }
}
