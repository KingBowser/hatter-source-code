package me.hatter.tools.resourceproxy.commons.io;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringPrintWriter extends PrintWriter {

    public StringPrintWriter() {
        super(new StringWriter());
    }

    public StringPrintWriter(int initialSize) {
        super(new StringWriter(initialSize));
    }

    public StringWriter getWriter() {
        flush();
        return (StringWriter) out;
    }

    public String toString() {
        return getWriter().toString();
    }
}
