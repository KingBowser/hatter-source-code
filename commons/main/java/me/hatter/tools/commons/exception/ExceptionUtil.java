package me.hatter.tools.commons.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

    public static String printStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static RuntimeException wrapRuntimeException(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        } else {
            return new RuntimeException(throwable);
        }
    }
}
