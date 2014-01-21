package me.hatter.tools.commons.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FileBufferedWriter extends BufferedWriter {

    public FileBufferedWriter(File file) throws UnsupportedEncodingException, FileNotFoundException {
        super(new OutputStreamWriter(new FileOutputStream(file), IOUtil.CHARSET_UTF8));
    }

    public FileBufferedWriter(File file, String charset) throws UnsupportedEncodingException, FileNotFoundException {
        super(new OutputStreamWriter(new FileOutputStream(file), charset));
    }
}
