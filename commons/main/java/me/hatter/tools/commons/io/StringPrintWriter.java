package me.hatter.tools.commons.io;

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

    public StringPrintWriter doPrint(String str) {
        this.print(str);
        return this;
    }

    public StringPrintWriter doPrintln(String str) {
        this.println(str);
        return this;
    }

    public String toString() {
        return getWriter().toString();
    }
}
