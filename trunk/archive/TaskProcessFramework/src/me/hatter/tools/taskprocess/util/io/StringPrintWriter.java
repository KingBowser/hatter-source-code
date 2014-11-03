package me.hatter.tools.taskprocess.util.io;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringPrintWriter extends PrintWriter {

    private StringWriter writer;

    public StringPrintWriter() {
        super(new StringWriter());
        writer = (StringWriter) this.out;
    }

    public String toString() {
        this.flush();
        return writer.toString();
    }
}
