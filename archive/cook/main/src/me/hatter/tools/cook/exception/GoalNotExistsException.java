package me.hatter.tools.cook.exception;

public class GoalNotExistsException extends CookException {

    private static final long serialVersionUID = 7125592300756583475L;

    public GoalNotExistsException() {
        super();
    }

    public GoalNotExistsException(String message) {
        super(message);
    }

    public GoalNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoalNotExistsException(Throwable cause) {
        super(cause);
    }
}
