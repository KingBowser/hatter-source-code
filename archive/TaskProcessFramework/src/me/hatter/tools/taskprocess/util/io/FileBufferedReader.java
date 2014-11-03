package me.hatter.tools.taskprocess.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import me.hatter.tools.taskprocess.util.env.Env;

public class FileBufferedReader extends BufferedReader {

    public FileBufferedReader(String fileName) throws IOException {
        this(Env.USER_DIR, fileName);
    }

    public FileBufferedReader(String basePath, String fileName) throws IOException {
        this(new File(basePath, fileName));
    }

    public FileBufferedReader(File file) throws IOException {
        super(new InputStreamReader(new FileInputStream(file), Env.UTF_8));
    }
}
