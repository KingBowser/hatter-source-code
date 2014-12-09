package me.hatter.tools.commons.encoding;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import me.hatter.tools.commons.encoding.utf7.UTF7Charset;

// <SCRIPT>alert(‘XSS’);</SCRIPT>
// +ADw-SCRIPT+AD4-alert('XSS');+ADw-/SCRIPT+AD4-
//
// http://www.motobit.com/util/charset-codepage-conversion.asp
// http://zh.wikipedia.org/wiki/UTF-7
// http://zh.wikipedia.org/wiki/Base64
// http://tools.ietf.org/html/rfc2152
// http://jutf7.sourceforge.net/license.html
// http://www.freeutils.net/source/jcharset/
public class UTF7Util {

    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        UTF7Charset u7c = new UTF7Charset();
        ByteBuffer bf = u7c.encode(str);
        byte[] b = new byte[bf.remaining()];
        bf.get(b);
        return new String(b); // now safe!
    }

    public static String decode(String str) {
        if (str == null) {
            return null;
        }
        UTF7Charset u7c = new UTF7Charset();
        CharBuffer cb = u7c.decode(ByteBuffer.wrap(str.getBytes()));
        char[] c = new char[cb.remaining()];
        cb.get(c);
        return new String(c);
    }
}
