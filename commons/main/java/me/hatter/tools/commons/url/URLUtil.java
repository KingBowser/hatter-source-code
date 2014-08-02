package me.hatter.tools.commons.url;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
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
            return (enc == null) ? encode(url) : URLEncoder.encode(url, enc);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHost(String url) throws URISyntaxException {
        url = url.replace('\\', '/');
        int indexOfQ = url.indexOf('?');
        if (indexOfQ > 0) {
            url = url.substring(0, indexOfQ);
        }
        int indexOfS = url.indexOf('#');
        if (indexOfS > 0) {
            url = url.substring(0, indexOfS);
        }
        return new URI(url).getHost();
    }
}
