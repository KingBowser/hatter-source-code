package me.hatter.tools.resourceproxy.jsspserver.session;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Session {

    /* package private */
    Date                        create = new Date();
    Date                        active = new Date();

    private String              sessionId;

    private Map<String, Object> map    = new HashMap<String, Object>();

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) map.get(key);
    }

    public <T> void setAttribute(String key, T value) {
        map.put(key, value);
    }

    public void removeAttribute(String key) {
        map.remove(key);
    }

    public void removeAll() {
        map.clear();
    }

    public String getSessionId() {
        return sessionId;
    }
}
