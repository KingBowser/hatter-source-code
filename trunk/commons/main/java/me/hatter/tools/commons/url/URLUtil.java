package me.hatter.tools.commons.url;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import me.hatter.tools.commons.io.IOUtil;

// http://src.chromium.org/svn/branches/WebKit/172/JavaScriptCore/runtime/JSGlobalObjectFunctions.cpp
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

    public static void main(String[] args) {
        System.out.println(encodeURI("http://aaa===+++.中国\r\n"));
        System.out.println(encodeURIComponent("http://aaa===+++.中国"));
        System.out.println(decodeURI(encodeURI("http://aaa===+++.中国")));
        System.out.println(decodeURIComponent(encodeURIComponent("http://aaa===+++.中国")));
    }

    public static String encodeURI(String uri) {
        return encode2(uri, "ABCDEFGHIJKLMNOPQRSTUVWXYZ" //
                            + "abcdefghijklmnopqrstuvwxyz" //
                            + "0123456789"//
                            + "!#$&'()*+,-./:;=?@_~");
    }

    public static String encodeURIComponent(String uri) {
        return encode2(uri, "ABCDEFGHIJKLMNOPQRSTUVWXYZ" //
                            + "abcdefghijklmnopqrstuvwxyz" //
                            + "0123456789" //
                            + "!'()*-._~");
    }

    public static String decodeURI(String uri) {
        return decode2(uri);
    }

    public static String decodeURIComponent(String uri) {
        return decode2(uri);
    }

    static String decode2(String str) {
        if (str == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            char nextc = (i + 1 < str.length()) ? str.charAt(i + 1) : 0x00;
            char next2c = (i + 2 < str.length()) ? str.charAt(i + 2) : 0x00;
            if (c == '%') {
                try {
                    int in = Integer.parseInt(new String(new char[] { nextc, next2c }), 16);
                    baos.write(new byte[] { (byte) in });
                    i += 2;
                } catch (Exception ex) {
                    try {
                        baos.write(new String(new char[] { c }).getBytes(IOUtil.CHARSET_UTF8));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                try {
                    baos.write(new String(new char[] { c }).getBytes(IOUtil.CHARSET_UTF8));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            return new String(baos.toByteArray(), IOUtil.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static String encode2(String str, String doNotEscape) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            boolean match = false;
            L_FOR: for (int j = 0; j < doNotEscape.length(); j++) {
                char nc = doNotEscape.charAt(j);
                if (c == nc) {
                    match = true;
                    break L_FOR;
                }
            }
            if (match) {
                sb.append(c);
            } else {
                try {
                    byte[] bs = new String(new char[] { c }).getBytes(IOUtil.CHARSET_UTF8);
                    for (byte b : bs) {
                        int x = ((int) b) & 0xFF;
                        String hx = Integer.toHexString(x).toUpperCase();
                        if (hx.length() == 1) {
                            sb.append("%0" + hx);
                        } else if (hx.length() == 2) {
                            sb.append("%" + hx);
                        } else {
                            throw new RuntimeException("Unbelieveable: " + hx);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("UTF-8 is not supported!");
                }
            }
        }
        return sb.toString();
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
