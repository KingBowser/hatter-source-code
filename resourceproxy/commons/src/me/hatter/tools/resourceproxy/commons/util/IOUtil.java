package me.hatter.tools.resourceproxy.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOUtil {

    public static String readToString(Reader reader) {
        StringBuilder sb = new StringBuilder();
        if (reader != null) {
            BufferedReader br = (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(
                                                                                                                  reader);
            try {
                for (int b; ((b = br.read()) != -1);) {
                    sb.append((char) b);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sb.toString();
    }

    public static byte[] readToBytes(InputStream inputStream) {
        InputStream fis = null;
        try {
            fis = (inputStream instanceof BufferedInputStream) ? (BufferedInputStream) inputStream : new BufferedInputStream(
                                                                                                                             inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtil.copy(fis, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.closeQuitely(fis);
        }
    }

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

    public static void closeQuitely(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            // EAT EXCEPTION
        }
    }
}
