package me.hatter.tools.commons.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import me.hatter.tools.commons.environment.Environment;

public class FileBufferedWriter extends BufferedWriter {

    public FileBufferedWriter(File file) throws UnsupportedEncodingException, FileNotFoundException {
        super(new OutputStreamWriter(new FileOutputStream(file), IOUtil.CHARSET_UTF8));
    }

    public FileBufferedWriter(File file, String charset) throws UnsupportedEncodingException, FileNotFoundException {
        super(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    public FileBufferedWriter doPrint(String str) {
        try {
            this.write(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public FileBufferedWriter doPrintln(String str) {
        doPrint(str).doPrint(Environment.LINE_SEPARATOR);
        return this;
    }
}
