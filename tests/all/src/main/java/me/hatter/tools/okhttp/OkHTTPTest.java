package me.hatter.tools.okhttp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.hatter.tools.commons.io.IOUtil;

import com.squareup.okhttp.OkHttpClient;

public class OkHTTPTest {

    static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws IOException {
        HttpURLConnection connection = client.open(new URL("https://www.google.com"));
        InputStream in = null;
        try {
//            connection.setRequestMethod("GET");
            in = connection.getInputStream();
            String s = IOUtil.readToString(in);
            System.out.println(s);
        } finally {
            IOUtil.closeQuitely(in);
        }
    }
}
