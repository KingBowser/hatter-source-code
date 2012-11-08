package me.hatter.jprofiler.exception;

public class ParseException extends RuntimeException {

    private static final long serialVersionUID = -4581290766872826627L;

    public ParseException() {
        super();
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}
