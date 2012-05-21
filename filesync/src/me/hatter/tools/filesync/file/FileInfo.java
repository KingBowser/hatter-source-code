package me.hatter.tools.filesync.file;

import java.io.File;
import java.util.Arrays;

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

    @Override
    public int hashCode() {
        return Arrays.asList((Object) file, path, signature).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if ((object == null) || (object.getClass() != FileInfo.class)) {
            return false;
        }
        FileInfo another = (FileInfo) object;
        return Arrays.asList((Object) this.file, this.path, this.signature).equals( //
        Arrays.asList((Object) another.file, another.path, another.signature));
    }
}
