package me.hatter.tools.resourceproxy.jsspserver.session;

public interface SessionEvent {

    void sessionCreate(Session session);

    void sessionDestory(Session session);
}
