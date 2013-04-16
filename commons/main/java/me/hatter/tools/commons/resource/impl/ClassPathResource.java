package me.hatter.tools.commons.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import me.hatter.tools.commons.resource.Resource;
import me.hatter.tools.commons.string.StringUtil;

public class ClassPathResource implements Resource {

    private static long INIT_MILLIS = System.currentTimeMillis();

    private URL         url;
    private String      resId;
    private long        lastModified;

    public ClassPathResource(URL url) {
        this(url, url.toString());
    }

    public ClassPathResource(URL url, String resId) {
        this.url = url;
        this.resId = resId;
        this.lastModified = INIT_MILLIS;
    }

    public ClassPathResource(URL url, String resId, long lastModified) {
        this.url = url;
        this.resId = resId;
        this.lastModified = lastModified;
    }

    public URL getURL() {
        return url;
    }

    @Override
    public String getResourceId() {
        return resId;
    }

    @Override
    public String getResourceName() {
        String name = resId;
        if (name.contains("/")) {
            name = StringUtil.substringAfterLast(name, "/");
        } else if (name.contains("\\")) {
            name = StringUtil.substringAfterLast(name, "\\");
        }
        return name;
    }

    @Override
    public boolean exists() {
        return (url != null);
    }

    @Override
    public long lastModified() {
        return lastModified;
    }

    @Override
    public InputStream openInputStream() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return (resId == null) ? 0 : resId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != ClassPathResource.class) {
            return false;
        }
        ClassPathResource another = (ClassPathResource) obj;
        if (this.resId == null) {
            return (another.resId == null);
        }
        return this.resId.equals(another.resId);
    }

    @Override
    public String toString() {
        return ClassPathResource.class.getSimpleName() + "{resId=" + resId + ", url:" + url + "}";
    }
}
