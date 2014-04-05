package me.hatter.tools.resourceproxy.jsspexec.util;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import me.hatter.tools.commons.url.URLUtil;
import me.hatter.tools.resourceproxy.commons.util.StringUtil;

public class BufferWriter {

    private StringWriter sw;
    private PrintWriter  pw;

    public BufferWriter() {
        this.sw = new StringWriter();
        this.pw = new PrintWriter(this.sw);
    }

    public BufferWriter(Writer pw) {
        this.pw = new PrintWriter(pw);
    }

    public BufferWriter(PrintWriter pw) {
        this.pw = pw;
    }

    public BufferWriter(OutputStream out, String encoding) {
        try {
            this.pw = new PrintWriter(new OutputStreamWriter(out, encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeEncodeUrl(String o) {
        pw.write(URLUtil.encode(objectToString(o)));
    }

    public void writeEscape(Object o) {
        pw.write(StringUtil.escapeHtml(objectToString(o)));
    }

    public void writePreText(Object o) {
        pw.write(StringUtil.preTextHtml(objectToString(o)));
    }

    public void write(Object o) {
        pw.write(objectToString(o));
    }

    public void flush() {
        pw.flush();
    }

    public void close() {
        pw.close();
    }

    public String getBufferedString() {
        if (sw == null) {
            throw new RuntimeException("StringWriter is null.");
        }
        pw.flush();
        return sw.toString();
    }

    private String objectToString(Object o) {
        if (o instanceof Double) {
            double doubleValue = ((Double) o).doubleValue();
            if (Double.compare(Math.ceil(doubleValue), Math.floor(doubleValue)) == 0) {
                o = Double.valueOf(doubleValue).longValue();
            }
        }
        return ((o == null) ? StringUtil.EMPTY : o.toString());
    }
}
