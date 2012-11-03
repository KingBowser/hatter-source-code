package me.hatter.tools.filesync.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.filesync.file.FileInfo;
import me.hatter.tools.filesync.signature.Signature;

public class FileListUtil {

    public static List<FileInfo> listFileInfos(File dir, FileFilter filter, Signature<File> signature) {
        List<File> files = listFiles(dir, filter);
        List<FileInfo> fileInfos = new ArrayList<FileInfo>(files.size());
        try {
            for (File f : files) {
                FileInfo fi = new FileInfo(f, f.getCanonicalPath().replace(dir.getCanonicalPath(), ""),
                                           signature.sign(f));
                fileInfos.add(fi);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileInfos;
    }

    public static List<File> listFiles(File dir, FileFilter filter) {
        List<File> refList = new ArrayList<File>();
        listFiles(dir, filter, refList);
        return refList;
    }

    private static void listFiles(File dir, FileFilter filter, List<File> refList) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (filter.accept(f)) {
                refList.add(f);
                if (f.isDirectory()) {
                    listFiles(dir, filter, refList);
                }
            }
        }
    }
}
