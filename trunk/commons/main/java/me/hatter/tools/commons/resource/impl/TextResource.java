package me.hatter.tools.commons.resource.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.hatter.tools.commons.resource.Resource;

public class TextResource implements Resource {

    private static long INIT_MILLIS = System.currentTimeMillis();

    private String      text;
    private String      resId;

    public TextResource(String text, String resId) {
        this.text = text;
        this.resId = resId;
    }

    // @Override
    public String getResId() {
        return resId;
    }

    // @Override
    public boolean exists() {
        return true;
    }

    // @Override
    public long lastModified() {
        return INIT_MILLIS;
    }

    // @Override
    public InputStream openInputStream() {
        try {
            return new ByteArrayInputStream(text.getBytes("UTF-8"));
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
        if (obj.getClass() != TextResource.class) {
            return false;
        }
        TextResource another = (TextResource) obj;
        if (this.resId == null) {
            return (another.resId == null);
        }
        return this.resId.equals(another.resId);
    }

    @Override
    public String toString() {
        return TextResource.class.getSimpleName() + "{resId=" + resId + ", text:" + text + "}";
    }

    @Override
    public Object getRaw() {
        return text;
    }
}
