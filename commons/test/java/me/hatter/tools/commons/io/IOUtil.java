package me.hatter.tools.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import me.hatter.tools.commons.exception.ExceptionUtil;

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
        try {
            return new String(readToBytes(inputStream), charset);
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    public static byte[] readToBytesAndClose(InputStream inputStream) {
        try {
            return readToBytes(inputStream);
        } finally {
            closeQuitely(inputStream);
        }
    }

    public static byte[] readToBytes(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            IOUtil.copy(inputStream, baos);
            baos.flush();
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
        return baos.toByteArray();
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
        try {
            writeBytes(outputStream, content.getBytes(charset));
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    public static void writeBytesAndClose(OutputStream outputStream, byte[] bytes) {
        try {
            writeBytes(outputStream, bytes);
        } finally {
            closeQuitely(outputStream);
        }
    }

    public static void writeBytes(OutputStream outputStream, byte[] bytes) {
        OutputStream bos = getBufferedOutputStream(outputStream);
        try {
            bos.write(bytes);
            bos.flush();
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
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
            // IGNORE EXCEPTION
        }
    }
}
