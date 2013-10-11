package me.hatter.tools.commons.resource;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.resource.impl.FileResource;
import me.hatter.tools.commons.resource.impl.URLResource;

public class Resources {

    public static final String DEF_RESOURCE = System.getProperty("def.resource");
    public static final String RESOURCE     = "resource:";
    public static final String CLASSPATH    = "classpath:";
    public static final String FILE         = "file:";

    private File               basePath     = null;

    public Resources(File basePath) {
        this.basePath = basePath;
    }

    public Resource findResource(String resId) {
        if (resId == null) {
            return null;
        }
        if (resId.startsWith(RESOURCE)) {
            if (isDefClasspath()) {
                URL url = Resources.class.getClassLoader().getResource(resId.substring(RESOURCE.length()));
                return new URLResource(url, resId);
            } else {
                File file;
                if (basePath == null) {
                    file = new File(resId.substring(RESOURCE.length()));
                } else {
                    file = new File(basePath, resId.substring(RESOURCE.length()));
                }
                return new FileResource(file, resId);
            }
        }
        if (resId.startsWith(CLASSPATH)) {
            URL url = Resources.class.getClassLoader().getResource(resId.substring(CLASSPATH.length()));
            return new URLResource(url, resId);
        }
        if (resId.startsWith(FILE)) {
            File file = new File(resId.substring(FILE.length()));
            return new FileResource(file, resId);
        }
        throw new RuntimeException("Unknown resource type: " + resId);
    }

    private static AtomicBoolean isDefClasspath;

    synchronized public static boolean isDefClasspath() {
        if (isDefClasspath == null) {
            isDefClasspath = new AtomicBoolean(false);
            if (DEF_RESOURCE != null) {
                isDefClasspath.set("classpath".equalsIgnoreCase(DEF_RESOURCE));
            } else {
                isDefClasspath.set(Resources.class.getClassLoader().getResource("def.resource.classpath") != null);
            }
        }
        return isDefClasspath.get();
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
            return IOUtil.readToString(new InputStreamReader(is, charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.closeQuietly(is);
        }
    }
}
