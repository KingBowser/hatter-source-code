package me.hatter.tools.filesync.file;

import java.io.File;

public class FileInfo {

    private File   file;
    private String path;
    private String signature;

    public FileInfo(File file, String path, String signature) {
        this.file = file;
        this.path = path;
        this.signature = signature;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isSignatureMatch(String signature) {
        if (this.signature == null) {
            return (signature == null);
        }
        return this.signature.equals(signature);
    }
}
