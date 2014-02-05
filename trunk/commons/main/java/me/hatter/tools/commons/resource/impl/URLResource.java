package me.hatter.tools.commons.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import me.hatter.tools.commons.resource.Resource;

public class URLResource implements Resource {

    private static long INIT_MILLIS = System.currentTimeMillis();

    private URL         url;
    private String      resId;
    private long        lastModified;

    public URLResource(URL url, String resId) {
        this.url = url;
        this.resId = resId;
        this.lastModified = INIT_MILLIS;
    }

    public URLResource(URL url, String resId, long lastModified) {
        this.url = url;
        this.resId = resId;
        this.lastModified = lastModified;
    }

    // @Override
    public String getResId() {
        return resId;
    }

    // @Override
    public boolean exists() {
        return (url != null);
    }

    // @Override
    public long lastModified() {
        return lastModified;
    }

    // @Override
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
        if (obj.getClass() != URLResource.class) {
            return false;
        }
        URLResource another = (URLResource) obj;
        if (this.resId == null) {
            return (another.resId == null);
        }
        return this.resId.equals(another.resId);
    }

    @Override
    public String toString() {
        return URLResource.class.getSimpleName() + "{resId=" + resId + ", url:" + url + "}";
    }
    
    public Object getRaw() {
        return url;
    }
}
