package me.hatter.tools.xmpprobots.biz;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.log.LogUtil;

public abstract class AbstractBusiness implements Business, LifeCycle {

    private List<MessageFilter> messageFilters = new ArrayList<MessageFilter>();

    public void init() throws Exception {
    }

    public void destory() throws Exception {
    }

    public void handleMessage(BusinessMessage message) {
        MessageFilter messageFilter = new AbstractMessageFilter() {

            private int index = 0;

            public void filter(BusinessMessage message, MessageFilter filter) {
                if (index < messageFilters.size()) {
                    try {
                        messageFilters.get(index).filter(message, this);
                    } catch (Throwable throwable) {
                        onHandleMessageError(message, throwable);
                        return; // error exit
                    }
                } else {
                    onNoHandleMessage(message);
                    return;
                }
                index++;
            }
        };
        try {
            messageFilter.filter(message, messageFilter);
        } catch (Throwable throwable) {
            onHandleMessageError(message, throwable);
        }
    }

    public void onNoHandleMessage(BusinessMessage message) {
        LogUtil.warn("Message not handled: " + message);
    }

    public void onHandleMessageError(BusinessMessage message, Throwable throwable) {
        LogUtil.error("Message handle error: " + message, throwable);
    }
}
