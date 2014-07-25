package me.hatter.tests.https;

import java.io.IOException;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

// https://developer.android.com/training/articles/security-ssl.html#CommonProblems
public class HttpsTest2 {

    public static void main(String[] args) throws IOException {
        SocketFactory sf = SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) sf.createSocket("github.com", 443);
        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        SSLSession s = socket.getSession();

        // Verify that the certicate hostname is for mail.google.com
        // This is due to lack of SNI support in the current SSLSocket.
        if (!hv.verify("github.com", s)) {
            throw new SSLHandshakeException("Expected github.com,  found " + s.getPeerPrincipal());
        }

        socket.close();
    }
}
