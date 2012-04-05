package me.hatter.tools.cook.exception;

public class CookInitException extends CookException {

    private static final long serialVersionUID = -3143715442176275406L;

    public CookInitException() {
        super();
    }

    public CookInitException(String message) {
        super(message);
    }

    public CookInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public CookInitException(Throwable cause) {
        super(cause);
    }
}
