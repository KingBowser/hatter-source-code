package me.hatter.tools.filesync.rmi;

import java.util.List;

import me.hatter.tools.filesync.file.FileInfo;

public interface FileService {

    List<FileInfo> listFileInfos();
}
