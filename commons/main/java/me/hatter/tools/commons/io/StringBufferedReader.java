package me.hatter.tools.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import me.hatter.tools.commons.function.IndexedProcedure;

public class StringBufferedReader extends BufferedReader {

    public StringBufferedReader(String s) {
        super(new StringReader(s));
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
