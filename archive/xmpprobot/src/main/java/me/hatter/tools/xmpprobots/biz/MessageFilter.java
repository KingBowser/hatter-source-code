package me.hatter.tools.xmpprobots.biz;

public interface MessageFilter extends LifeCycle {

    void filter(BusinessMessage message, MessageFilter filter) throws Exception;
}
