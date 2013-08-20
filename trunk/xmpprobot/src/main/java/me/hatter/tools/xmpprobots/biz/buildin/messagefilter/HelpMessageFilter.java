package me.hatter.tools.xmpprobots.biz.buildin.messagefilter;

import me.hatter.tools.xmpprobots.biz.AbstractMessageFilter;
import me.hatter.tools.xmpprobots.biz.BusinessMessage;
import me.hatter.tools.xmpprobots.biz.MessageFilter;

public class HelpMessageFilter extends AbstractMessageFilter {

    public void filter(BusinessMessage message, MessageFilter filter) throws Exception {
        if (message.getCmd().equals("help")) {
            message.getChat().sendMessage("");
        } else {
            filter.filter(message, filter);
        }
    }
}
