package me.hatter.tools.commons.resource.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import me.hatter.tools.commons.resource.Resource;
import me.hatter.tools.commons.string.StringUtil;

public class ZipEntryResource implements Resource {

    private ZipFile  zipFile;
    private ZipEntry zipEntry;
    private String   resId;

    public ZipEntryResource(ZipFile zipFile, ZipEntry zipEntry) {
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
        this.resId = "file:" + zipFile.getName() + "!/" + zipEntry.getName();
    }

    public ZipFile getZipFile() {
        return zipFile;
    }

    public ZipEntry getZipEntry() {
        return zipEntry;
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
    public InputStream openInputStream() {
        try {
            return zipFile.getInputStream(zipEntry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists() {
        return new File(zipFile.getName()).exists();
    }

    @Override
    public long lastModified() {
        return new File(zipFile.getName()).lastModified();
    }
}
