package me.hatter.tools.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;

public class IOUtil {

    public static final String CHARSET_UTF8 = "UTF-8";

    public static String readResourceToString(Class<?> clazz, String resourceName) {
        InputStream is = clazz.getResourceAsStream(resourceName);
        if (is == null) {
            throw new RuntimeException("Resource not found(by class): " + resourceName);
        }
        return readToStringAndClose(is);
    }

    public static String readResourceToString(ClassLoader classLoader, String resourceName) {
        InputStream is = classLoader.getResourceAsStream(resourceName);
        if (is == null) {
            throw new RuntimeException("Resource not found(by classloader): " + resourceName);
        }
        return readToStringAndClose(is);
    }

    public static String readToStringAndClose(InputStream inputStream) {
        return readToStringAndClose(inputStream, CHARSET_UTF8);
    }

    public static String readToStringAndClose(InputStream inputStream, String charset) {
        try {
            return readToString(inputStream, charset);
        } finally {
            closeQuitely(inputStream);
        }
    }

    public static String readToString(InputStream inputStream) {
        return readToString(inputStream, CHARSET_UTF8);
    }

    public static String readToString(InputStream inputStream, String charset) {
        StringWriter sw = new StringWriter();
        InputStream bis = getBufferedInputStream(inputStream);
        try {
            InputStreamReader reader = new InputStreamReader(bis, charset);
            for (int c; ((c = reader.read()) != -1);) {
                sw.write(c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sw.flush();
        return sw.toString();
    }

    public static void writeStringAndClose(OutputStream outputStream, String content) {
        writeStringAndClose(outputStream, content, CHARSET_UTF8);
    }

    public static void writeStringAndClose(OutputStream outputStream, String content, String charset) {
        try {
            writeString(outputStream, content, charset);
        } finally {
            closeQuitely(outputStream);
        }
    }

    public static void writeString(OutputStream outputStream, String content) {
        writeString(outputStream, content, CHARSET_UTF8);
    }

    public static void writeString(OutputStream outputStream, String content, String charset) {
        OutputStream bos = getBufferedOutputStream(outputStream);
        try {
            bos.write(content.getBytes(charset));
            bos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // public static

    public static InputStream getBufferedInputStream(InputStream inputStream) {
        if (inputStream instanceof BufferedInputStream) {
            return (BufferedInputStream) inputStream;
        } else {
            return new BufferedInputStream(inputStream);
        }
    }

    public static OutputStream getBufferedOutputStream(OutputStream outputStream) {
        if (outputStream instanceof BufferedOutputStream) {
            return (BufferedOutputStream) outputStream;
        } else {
            return new BufferedOutputStream(outputStream);
        }
    }

    public static void closeQuitely(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            // IGNORE EXCEPTION
        }
    }
}
