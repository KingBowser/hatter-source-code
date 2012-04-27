package me.hatter.tools.filesync.file;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FileInfoCache implements Serializable {

    private static final long   serialVersionUID = -5240995700913367757L;

    private Map<File, FileInfo> fileInfoMap      = new HashMap<File, FileInfo>();

    public Map<File, FileInfo> getFileInfoMap() {
        return fileInfoMap;
    }

    public void setFileInfoMap(Map<File, FileInfo> fileInfoMap) {
        this.fileInfoMap = fileInfoMap;
    }
}
