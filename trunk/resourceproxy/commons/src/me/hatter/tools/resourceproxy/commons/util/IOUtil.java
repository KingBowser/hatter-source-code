package me.hatter.tools.resourceproxy.commons.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class IOUtil {

    public static long copy(InputStream is, OutputStream os) throws IOException {
        long total = 0;
        for (int b = 0; ((b = is.read()) != -1);) {
            os.write(b);
            total++;
        }
        return total;
    }

    public static long copy(Reader reader, Writer writer) throws IOException {
        long total = 0;
        for (int c; ((c = reader.read()) != -1);) {
            writer.write(c);
        }
        return total;
    }

    public static String copyToString(Reader reader) throws IOException {
        StringWriter sw = new StringWriter();
        copy(reader, sw);
        return sw.toString();
    }

    public static void closeQuitely(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            // EAT EXCEPTION
        }
    }
}
