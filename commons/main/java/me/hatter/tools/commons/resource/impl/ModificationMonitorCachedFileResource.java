package me.hatter.tools.commons.resource.impl;

import java.io.File;

public class ModificationMonitorCachedFileResource extends AbstractCachedResource {

    private long lastModify = 0L;
    private long lastSize   = 0L;

    public ModificationMonitorCachedFileResource(FileResource targetResource) {
        super(targetResource);
    }

    @Override
    protected boolean needUpdateCache() {
        if (lastModify == 0L) {
            return true;
        }
        long fileLastModify = ((File) getTargetResource().getRaw()).lastModified();
        long fileSize = ((File) getTargetResource().getRaw()).length();
        return (!((fileLastModify == lastModify) && (lastSize == fileSize)));
    }

    @Override
    protected void afterUpdateCache() {
        lastModify = ((File) getTargetResource().getRaw()).lastModified();
        lastSize = ((File) getTargetResource().getRaw()).length();
    }
}
