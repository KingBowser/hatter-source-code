package me.hatter.tools.jsspserver.exception;

// WARNING: no stack trace all sub classes
public abstract class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public synchronized Throwable fillInStackTrace() {
        // DO NOTHING; ONLY MAKE FAST
        return null;
    }
}
