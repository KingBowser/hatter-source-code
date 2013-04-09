package me.hatter.tools.commons.url;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import me.hatter.tools.commons.misc.Base64;

public class BasicAuthURL {

    private String userName;
    private String password;

    public BasicAuthURL(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public URLConnection makeConnection(String url) throws IOException {
        URL u = new URL(url);
        URLConnection urlConnection = u.openConnection();
        String auth = userName + ":" + password;
        urlConnection.setRequestProperty("Authorization", "Basic " + Base64.byteArrayToBase64(auth.getBytes()));
        return urlConnection;
    }
}
