package me.hatter.tools.resourceproxy.jsspexec.exception;

public class JsspEvalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JsspEvalException() {
        super();
    }

    public JsspEvalException(String message) {
        super(message);
    }

    public JsspEvalException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsspEvalException(Throwable cause) {
        super(cause);
    }
}
