package me.hatter.tools.commons.mime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.hatter.tools.commons.string.StringUtil;

public class MimeUtil {

    private static final Map<String, String> mimeTypeMap = new HashMap<String, String>();
    static {
        mimeTypeMap.put("text", "text/plain");
        mimeTypeMap.put("htm", "text/html");
        mimeTypeMap.put("html", "text/html");
        mimeTypeMap.put("xml", "application/xml");

        mimeTypeMap.put("gif", "image/gif");
        mimeTypeMap.put("jpg", "image/jpeg");
        mimeTypeMap.put("jpe", "image/jpeg");
        mimeTypeMap.put("jpeg", "image/jpeg");
        mimeTypeMap.put("png", "image/png");

        mimeTypeMap.put("css", "text/css");
        mimeTypeMap.put("csv", "text/csv");
        mimeTypeMap.put("js", "application/javascript");
        mimeTypeMap.put("json", "application/json");
        mimeTypeMap.put("pdf", "application/pdf");
    }

    public static boolean isTextMimeType(String mimeType) {
        if (mimeType != null) {
            mimeType = mimeType.trim().toLowerCase(Locale.US);
            if (mimeType.startsWith("text/")) {
                return true;
            }
            return Arrays.asList("application/javascript", "application/json").contains(mimeType);
        }
        return false;
    }

    public static boolean isTextExt(String ext) {
        return isTextMimeType(getMimeByExt(ext));
    }

    public static boolean isImageMimeType(String mimeType) {
        if (mimeType != null) {
            return mimeType.trim().toLowerCase(Locale.US).startsWith("image/");
        }
        return false;
    }

    public static boolean isImageExt(String ext) {
        return isImageMimeType(getMimeByExt(ext));
    }

    public static String getMimeByExt(String ext) {
        if (ext != null) {
            ext = ext.trim().toLowerCase(Locale.US);
            if (ext.contains(".")) {
                ext = StringUtil.substringAfterLast(ext, ".");
            }
            String mimeType = mimeTypeMap.get(ext);
            if (mimeType != null) {
                return mimeType;
            }
        }
        return "application/octet-stream";
    }
}
