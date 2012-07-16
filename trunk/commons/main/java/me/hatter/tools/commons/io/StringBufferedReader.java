package me.hatter.tools.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

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
}
