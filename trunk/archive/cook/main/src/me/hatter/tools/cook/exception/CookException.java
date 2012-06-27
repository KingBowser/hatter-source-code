package me.hatter.tools.cook.exception;

public class CookException extends Exception {

    private static final long serialVersionUID = -853402259493127678L;

    public CookException() {
        super();
    }

    public CookException(String message) {
        super(message);
    }

    public CookException(String message, Throwable cause) {
        super(message, cause);
    }

    public CookException(Throwable cause) {
        super(cause);
    }
}
