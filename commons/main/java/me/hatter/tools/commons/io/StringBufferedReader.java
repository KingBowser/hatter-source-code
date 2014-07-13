package me.hatter.tools.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import me.hatter.tools.commons.assertion.AssertUtil;
import me.hatter.tools.commons.collection.IteratorTool;

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
