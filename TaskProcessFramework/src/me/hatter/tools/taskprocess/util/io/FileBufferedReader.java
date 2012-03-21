package me.hatter.tools.taskprocess.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileBufferedReader extends BufferedReader {

    public FileBufferedReader(File file) throws IOException {
        super(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    }
}
