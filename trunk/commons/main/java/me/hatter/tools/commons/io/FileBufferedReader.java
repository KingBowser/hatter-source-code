package me.hatter.tools.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import me.hatter.tools.commons.assertion.AssertUtil;
import me.hatter.tools.commons.collection.IteratorTool;

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

    public IteratorTool<String> toIteratorTool() {
        return IteratorTool.from(new Iterator<String>() {

            private String nextLine = readOneLine();

            @Override
            public boolean hasNext() {
                return (nextLine != null);
            }

            @Override
            public String next() {
                AssertUtil.isTrue(hasNext());
                String line = nextLine;
                nextLine = readOneLine();
                return line;
            }

            @Override
            public void remove() {
                throw new IllegalStateException(this.getClass().getSimpleName() + " has no remove()");
            }
        });
    }
}
