package me.hatter.tools.cook.exception;

public class GoalInitException extends CookInitException {

    private static final long serialVersionUID = 2373202730801209690L;

    public GoalInitException() {
        super();
    }

    public GoalInitException(String message) {
        super(message);
    }

    public GoalInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoalInitException(Throwable cause) {
        super(cause);
    }
}
