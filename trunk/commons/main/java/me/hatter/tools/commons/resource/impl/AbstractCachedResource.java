package me.hatter.tools.commons.resource.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import me.hatter.tools.commons.exception.ExceptionUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.resource.Resource;

abstract public class AbstractCachedResource implements Resource {

    private Resource  targetResource;
    private boolean   exists                   = false;
    private long      lastModified             = 0L;
    private byte[]    bytes                    = null;
    private Exception openInputStreamException = null;

    public AbstractCachedResource(Resource targetResource) {
        this.targetResource = targetResource;
    }

    @Override
    public String getResId() {
        return targetResource.getResId();
    }

    @Override
    public boolean exists() {
        tryUpdateCache();
        return exists;
    }

    @Override
    public long lastModified() {
        tryUpdateCache();
        return lastModified;
    }

    @Override
    public InputStream openInputStream() {
        tryUpdateCache();
        if (openInputStreamException != null) {
            throw ExceptionUtil.wrapRuntimeException(openInputStreamException);
        }
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public Object getRaw() {
        return targetResource.getRaw();
    }

    protected Resource getTargetResource() {
        return targetResource;
    }

    synchronized protected void tryUpdateCache() {
        if (needUpdateCache()) {
            updateCache();
            afterUpdateCache();
        }
    }

    protected void updateCache() {
        this.exists = targetResource.exists();
        this.lastModified = targetResource.lastModified();
        try {
            this.bytes = IOUtil.readToBytesAndClose(targetResource.openInputStream());
        } catch (Exception e) {
            this.openInputStreamException = e;
        }
    }

    protected abstract boolean needUpdateCache();

    protected abstract void afterUpdateCache();
}
