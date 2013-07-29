package me.hatter.tests.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;

import me.hatter.tools.commons.io.SysOutUtil;

@SuppressWarnings("unchecked")
public class ChunkedOutputStream extends BufferedOutputStream {

    public ChunkedOutputStream(OutputStream out) {
        super(out);
    }

    public ChunkedOutputStream(OutputStream out, int size) {
        super(out, size);
    }

    public synchronized void flush() throws IOException {
        if (count != 0) {
            writeBuf(buf, 0, count);
            count = 0;
        }
    }

    private Vector footerNames  = new Vector();
    private Vector footerValues = new Vector();

    public void setFooter(String name, String value) {
        footerNames.addElement(name);
        footerValues.addElement(value);
    }

    public void done() throws IOException {
        flush();
        PrintStream pout = new PrintStream(out);
        pout.println("0");
        if (footerNames.size() > 0) {
            // Send footers.
            for (int i = 0; i < footerNames.size(); ++i) {
                String name = (String) footerNames.elementAt(i);
                String value = (String) footerValues.elementAt(i);
                pout.println(name + ": " + value);
            }
        }
        footerNames = null;
        footerValues = null;
        pout.println("");
        pout.flush();
    }

    // / Make sure that calling close() terminates the chunked stream.
    public void close() throws IOException {
        if (footerNames != null) done();
        super.close();
    }

    public synchronized void write(byte b[], int off, int len) throws IOException {
        int avail = buf.length - count;

        if (len <= avail) {
            System.arraycopy(b, off, buf, count, len);
            count += len;
            return;
        }
        flush();
        writeBuf(b, off, len);
    }

    private static final byte[] crlf = { 13, 10 };

    void writeBuf(byte b[], int off, int len) throws IOException {
        String lenStr = Integer.toString(len, 16);
        byte[] lenBytes = lenStr.getBytes();
        out.write(lenBytes);
        out.write(crlf);
        if (len != 0) out.write(b, off, len);
        out.write(crlf);
        out.flush();
        System.out.println("$" + lenBytes);
    }
}
