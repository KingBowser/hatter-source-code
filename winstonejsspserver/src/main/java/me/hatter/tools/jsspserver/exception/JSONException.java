package me.hatter.tools.jsspserver.exception;

public class JSONException extends BusinessException {

    private static final long serialVersionUID = 1L;

    private Object            object;

    public JSONException(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
