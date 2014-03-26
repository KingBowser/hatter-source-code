package me.hatter.tools.commons.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import me.hatter.tools.commons.resource.Resource;

public class ZipEntryResource implements Resource {

    private static long INIT_MILLIS = System.currentTimeMillis();

    private ZipFile     zipFile;
    private ZipEntry    zipEntry;
    private String      resId;
    private long        lastModified;

    public ZipEntryResource(ZipFile zipFile, ZipEntry zipEntry) {
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
        this.resId = "zip://" + zipFile.getName() + "?" + zipEntry.getName();
        this.lastModified = INIT_MILLIS;
    }

    @Override
    public String getResId() {
        return resId;
    }

    @Override
    public boolean exists() {
        return ((zipFile != null) && (zipEntry != null));
    }

    @Override
    public long lastModified() {
        return lastModified;
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
    public Object getRaw() {
        return new Object[] { zipFile, zipEntry };
    }

}
