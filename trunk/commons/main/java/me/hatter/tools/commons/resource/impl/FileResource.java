package me.hatter.tools.commons.resource.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import me.hatter.tools.commons.resource.Resource;
import me.hatter.tools.commons.string.StringUtil;

public class FileResource implements Resource {

    private File   file;
    private String resId;

    public FileResource(File file) {
        this(file, "file:" + file.getPath());
    }

    public FileResource(File file, String resId) {
        this.file = file;
        this.resId = resId;
    }

    public File getFile() {
        return file;
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
        return file.exists();
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public InputStream openInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
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
        if (obj.getClass() != FileResource.class) {
            return false;
        }
        FileResource another = (FileResource) obj;
        if (this.resId == null) {
            return (another.resId == null);
        }
        return this.resId.equals(another.resId);
    }

    @Override
    public String toString() {
        return FileResource.class.getSimpleName() + "{resId=" + resId + ", file:" + file + "}";
    }
}
