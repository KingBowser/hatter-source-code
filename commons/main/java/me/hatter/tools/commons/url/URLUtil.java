package me.hatter.tools.commons.url;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLUtil {

    @SuppressWarnings("deprecation")
    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(url);
        }
    }

    public static String encode(String url, String enc) {
        try {
            return URLEncoder.encode(url, enc);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
