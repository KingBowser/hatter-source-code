package me.hatter.tools.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import me.hatter.tools.commons.function.IndexedProcedure;

public class FileBufferedReader extends BufferedReader {

    public FileBufferedReader(File file) throws UnsupportedEncodingException, FileNotFoundException {
        super(new InputStreamReader(new FileInputStream(file), IOUtil.CHARSET_UTF8));
    }

    public FileBufferedReader(File file, String charset) throws UnsupportedEncodingException, FileNotFoundException {
        super(new InputStreamReader(new FileInputStream(file), charset));
    }

    public String readOneLine() {
        try {
            return super.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void each(IndexedProcedure<String> indexedProcedure) {
        int i = 0;
        for (String line; ((line = readOneLine()) != null); i++) {
            indexedProcedure.apply(line, i);
        }
    }
}
