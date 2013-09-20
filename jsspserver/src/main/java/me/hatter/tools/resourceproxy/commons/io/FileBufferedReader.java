package me.hatter.tools.resourceproxy.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileBufferedReader extends BufferedReader {

    public FileBufferedReader(File file) throws FileNotFoundException {
        super(new InputStreamReader(new FileInputStream(file)));
    }

    public FileBufferedReader(File file, String charset) throws UnsupportedEncodingException, FileNotFoundException {
        super(new InputStreamReader(new FileInputStream(file), charset));
    }
}
