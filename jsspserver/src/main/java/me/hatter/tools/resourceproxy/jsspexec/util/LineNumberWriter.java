package me.hatter.tools.resourceproxy.jsspexec.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class LineNumberWriter {

    private int         currentLine = 0;
    private PrintWriter pw;

    public LineNumberWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        pw = new PrintWriter(file, csn);
    }

    public LineNumberWriter(Writer w) {
        pw = new PrintWriter(w);
    }

    public void flush() {
        pw.flush();
    }

    public void close() {
        pw.close();
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void write(String s) {
        if (s != null) {
            pw.write(s);
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                char nc = ((i + 1) < s.length()) ? s.charAt(i + 1) : (char) 0;
                if (c == '\n') {
                    currentLine++;
                } else if (c == '\r') {
                    currentLine++;
                    if (nc == '\n') {
                        i++;
                    }
                }
            }
        }
    }
}
