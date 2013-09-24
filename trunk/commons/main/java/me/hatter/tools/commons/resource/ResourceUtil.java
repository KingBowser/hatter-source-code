package me.hatter.tools.commons.resource;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.resource.impl.ClassPathResource;
import me.hatter.tools.commons.resource.impl.FileResource;

public class ResourceUtil {

    public static final String CLASSPATH = "classpath:";
    public static final String FILE      = "file:";

    public Resource findResource(String resId) {
        if (resId == null) {
            return null;
        }
        if (resId.startsWith(CLASSPATH)) {
            URL url = ClassLoaderUtil.getContextClassLoader().getResource(resId.substring(CLASSPATH.length()));
            return new ClassPathResource(url, resId);
        }
        if (resId.startsWith(FILE)) {
            File file = new File(resId.substring(FILE.length()));
            return new FileResource(file, resId);
        }
        throw new RuntimeException("Unknown resource type: " + resId);
    }

    public static byte[] readToBytes(Resource resource) {
        InputStream is = resource.openInputStream();
        try {
            return IOUtil.readToBytes(is);
        } finally {
            IOUtil.closeQuietly(is);
        }
    }

    public static String readToString(Resource resource, String charset) {
        InputStream is = resource.openInputStream();
        try {
            return IOUtil.readToString(is, charset);
        } finally {
            IOUtil.closeQuietly(is);
        }
    }
}
