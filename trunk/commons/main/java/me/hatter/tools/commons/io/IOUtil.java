package me.hatter.tools.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.exception.ExceptionUtil;

public class IOUtil {

    public static final int    KB           = 1024;
    public static final int    MB           = KB * KB;
    public static final int    GB           = MB * KB;
    public static final int    TB           = GB * KB;
    public static final String CHARSET_UTF8 = "UTF-8";

    public static String readResourceBySystemClassloaderToString(String resourceName) {
        return readResourceToString(ClassLoaderUtil.getSystemClassLoader(), resourceName);
    }

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

    public static String readToStringAndClose(InputStream inputStream) {
        return readToStringAndClose(inputStream, CHARSET_UTF8);
    }

    public static String readToStringAndClose(InputStream inputStream, String charset) {
        try {
            return readToString(inputStream, charset);
        } finally {
            closeQuietly(inputStream);
        }
    }

    public static String readToString(Reader reader) {
        StringBuilder sb = new StringBuilder();
        if (reader != null) {
            BufferedReader br = asBufferedReader(reader);
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

    public static String readToStringAndClose(Reader reader) {
        try {
            return readToString(reader);
        } finally {
            closeQuietly(reader);
        }
    }

    public static List<String> readToList(String text) {
        List<String> list = new ArrayList<String>();
        StringBufferedReader reader = new StringBufferedReader(text);
        for (String line; ((line = reader.readOneLine()) != null);) {
            list.add(line);
        }
        return list;
    }

    public static String writeFromList(List<String> list) {
        StringPrintWriter writer = new StringPrintWriter();
        if (list != null) {
            for (String line : list) {
                writer.println(line);
            }
        }
        return writer.toString();
    }

    public static byte[] readToBytesAndClose(InputStream inputStream) {
        try {
            return readToBytes(inputStream);
        } finally {
            closeQuietly(inputStream);
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
            closeQuietly(outputStream);
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
            closeQuietly(outputStream);
        }
    }

    public static void writeBytes(OutputStream outputStream, byte[] bytes) {
        OutputStream bos = asBufferedOutputStream(outputStream);
        try {
            bos.write(bytes);
            bos.flush();
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    @Deprecated
    public static InputStream getBufferedInputStream(InputStream inputStream) {
        if (inputStream instanceof BufferedInputStream) {
            return (BufferedInputStream) inputStream;
        } else {
            return new BufferedInputStream(inputStream);
        }
    }

    @Deprecated
    public static OutputStream getBufferedOutputStream(OutputStream outputStream) {
        if (outputStream instanceof BufferedOutputStream) {
            return (BufferedOutputStream) outputStream;
        } else {
            return new BufferedOutputStream(outputStream);
        }
    }

    // copy block ----------------------------------------------------------------------------------------
    public static long copy(InputStream is, OutputStream os) throws IOException {
        long total = 0;
        byte[] b = new byte[8 * KB];
        for (int len; ((len = is.read(b)) != -1);) {
            os.write(b, 0, len);
            total += len;
        }
        return total;
    }

    public static long copyOneByOne(InputStream is, OutputStream os) throws IOException {
        long total = 0;
        for (int b = 0; ((b = is.read()) != -1);) {
            os.write(b);
            total++;
        }
        return total;
    }

    public static long copy(Reader reader, Writer writer) throws IOException {
        long total = 0;
        char[] c = new char[8 * KB];
        for (int len; ((len = reader.read(c)) != -1);) {
            writer.write(c, 0, len);
            total += len;
        }
        return total;
    }

    public static long copyOneByOne(Reader reader, Writer writer) throws IOException {
        long total = 0;
        for (int c; ((c = reader.read()) != -1);) {
            writer.write(c);
        }
        return total;
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            // IGNORE EXCEPTION
        }
    }

    // to block ----------------------------------------------------------------------------------------
    public static BufferedReader toBufferedReader(InputStream is) {
        return toBufferedReader(is, CHARSET_UTF8);
    }

    public static BufferedReader toBufferedReader(InputStream is, String charset) {
        try {
            return new BufferedReader(new InputStreamReader(is, charset));
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    public static BufferedWriter toBufferedWriter(OutputStream os) {
        return toBufferedWriter(os, CHARSET_UTF8);
    }

    public static BufferedWriter toBufferedWriter(OutputStream os, String charset) {
        try {
            return new BufferedWriter(new OutputStreamWriter(os, charset));
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    public static PrintWriter toPrintWriter(OutputStream os) {
        return toPrintWriter(os, CHARSET_UTF8);
    }

    public static PrintWriter toPrintWriter(OutputStream os, String charset) {
        try {
            return new PrintWriter(new OutputStreamWriter(os, charset));
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.wrapRuntimeException(e);
        }
    }

    // as block ----------------------------------------------------------------------------------------
    public static PrintWriter asPrintWriter(Writer writer) {
        if (writer instanceof PrintWriter) {
            return (PrintWriter) writer;
        } else {
            return new PrintWriter(writer);
        }
    }

    public static BufferedReader asBufferedReader(Reader reader) {
        if (reader instanceof BufferedReader) {
            return (BufferedReader) reader;
        } else {
            return new BufferedReader(reader);
        }
    }

    public static BufferedWriter asBufferedWriter(Writer writer) {
        if (writer instanceof BufferedWriter) {
            return (BufferedWriter) writer;
        } else {
            return new BufferedWriter(writer);
        }
    }

    public static BufferedInputStream asBufferedInputStream(InputStream inputStream) {
        if (inputStream instanceof BufferedInputStream) {
            return (BufferedInputStream) inputStream;
        } else {
            return new BufferedInputStream(inputStream);
        }
    }

    public static BufferedOutputStream asBufferedOutputStream(OutputStream outputStream) {
        if (outputStream instanceof BufferedOutputStream) {
            return (BufferedOutputStream) outputStream;
        } else {
            return new BufferedOutputStream(outputStream);
        }
    }
}
