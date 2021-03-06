package me.hatter.tools.commons.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FilePrintWriter extends PrintWriter {

    public FilePrintWriter(File file) throws UnsupportedEncodingException, FileNotFoundException {
        super(new OutputStreamWriter(new FileOutputStream(file), IOUtil.CHARSET_UTF8));
    }

    public FilePrintWriter(File file, String charset) throws UnsupportedEncodingException, FileNotFoundException {
        super(new OutputStreamWriter(new FileOutputStream(file), charset));
    }
}
