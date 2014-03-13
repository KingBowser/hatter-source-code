package me.hatter.tools.xmpprobots.biz;

public interface Business extends LifeCycle {

    String getName();

    String getNameSpace();

    String getDescription();

    void handleMessage(BusinessMessage message);

    void onNoHandleMessage(BusinessMessage message);

    void onHandleMessageError(BusinessMessage message, Throwable throwable);
}
