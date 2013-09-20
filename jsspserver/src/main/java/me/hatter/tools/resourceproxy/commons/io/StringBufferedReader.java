package me.hatter.tools.resourceproxy.commons.io;

import java.io.BufferedReader;
import java.io.StringReader;

public class StringBufferedReader extends BufferedReader {

    public StringBufferedReader(String s) {
        super(new StringReader(s));
    }
}
