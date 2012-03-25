package me.hatter.tools.taskprocess.util.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import me.hatter.tools.taskprocess.util.env.Env;

public class FilePrintWriter extends PrintWriter {

    public FilePrintWriter(String fileName) throws IOException {
        this(Env.USER_DIR, fileName);
    }

    public FilePrintWriter(String fileName, boolean append) throws IOException {
        this(Env.USER_DIR, fileName, append);
    }

    public FilePrintWriter(String basePath, String fileName) throws IOException {
        this(new File(basePath, fileName));
    }

    public FilePrintWriter(String basePath, String fileName, boolean append) throws IOException {
        this(new File(basePath, fileName), append);
    }

    public FilePrintWriter(File file) throws IOException {
        this(file, true);
    }

    public FilePrintWriter(File file, boolean append) throws IOException {
        super(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file, append)), Env.UTF_8));
    }
}
