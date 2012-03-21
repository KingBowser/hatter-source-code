package me.hatter.tools.taskprocess.util.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import me.hatter.tools.taskprocess.util.env.Env;

public class FilePrintWriter extends PrintWriter {

    public FilePrintWriter(File file) throws IOException {
        super(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), Env.UTF_8));
    }

    public FilePrintWriter(File file, boolean append) throws IOException {
        super(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file, append)), Env.UTF_8));
    }
}
