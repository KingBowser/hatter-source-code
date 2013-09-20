package me.hatter.tools.resourceproxy.jsspserver.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    private static final long         ACTIVE_TIME      = TimeUnit.MINUTES.toMillis(30);

    private static final long         DEAD_TIME        = TimeUnit.MINUTES.toMillis(40);

    private static final String       SESSION_ID_CHARS = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                                         + "0123456789";

    private static final int          SESSION_ID_LENG  = 64;

    private static List<SessionEvent> sessionEventList = new ArrayList<SessionEvent>();
    static {
        for (SessionEvent se : ServiceLoader.load(SessionEvent.class)) {
            sessionEventList.add(se);
        }
    }

    private Map<String, Session>      activePool       = new HashMap<String, Session>();

    private Map<String, Session>      deadPool         = new HashMap<String, Session>();

    private Random                    random           = new Random(new Date().getTime());

    private static SessionManager     sessionManager   = new SessionManager();

    private SessionManager() {
    }

    public static SessionManager getSessionManager() {
        return sessionManager;
    }

    synchronized public String generateSessionId() {
        StringBuilder sb = new StringBuilder(SESSION_ID_LENG);
        for (int i = 0; i < SESSION_ID_LENG; i++) {
            sb.append(SESSION_ID_CHARS.charAt(random.nextInt(SESSION_ID_CHARS.length())));
        }
        random = new Random(random.nextInt() + new Date().getTime());
        return sb.toString();
    }

    synchronized public Session getSession(String sessionId) {
        Session session = activePool.get(sessionId);
        if (session == null) {
            session = new Session(sessionId);
            activePool.put(sessionId, session);
            if (sessionEventList != null) {
                for (SessionEvent sessionEvent : sessionEventList) {
                    sessionEvent.sessionCreate(session);
                }
            }
        }
        session.active = new Date();
        return session;
    }

    synchronized public void reflushSession() {
        String[] activeKeys = activePool.keySet().toArray(new String[0]);
        for (String key : activeKeys) {
            Session session = activePool.get(key);
            if ((new Date().getTime() - session.active.getTime()) > ACTIVE_TIME) {
                activePool.remove(key);
                deadPool.put(key, session);
                if (sessionEventList != null) {
                    for (SessionEvent sessionEvent : sessionEventList) {
                        sessionEvent.sessionDestory(session);
                    }
                }
            }
        }

        String[] deadKeys = deadPool.keySet().toArray(new String[0]);
        for (String key : deadKeys) {
            Session session = deadPool.get(key);
            if ((new Date().getTime() - session.active.getTime()) > DEAD_TIME) {
                deadPool.remove(key);
            }
        }
    }

    public void startupSessionTask() {
        new SessionTaskThread(this).start();
    }
}

class SessionTaskThread extends Thread {

    private SessionManager sessionManager;

    public SessionTaskThread(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {
        while (true) {
            sessionManager.reflushSession();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
