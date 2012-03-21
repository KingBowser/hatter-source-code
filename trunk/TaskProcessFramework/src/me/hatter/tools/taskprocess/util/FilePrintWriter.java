package me.hatter.tools.taskprocess.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FilePrintWriter extends PrintWriter {

    public FilePrintWriter(File file) throws IOException {
        super(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), "UTF-8"));
    }

    public FilePrintWriter(File file, boolean append) throws IOException {
        super(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file, append)), "UTF-8"));
    }
}
