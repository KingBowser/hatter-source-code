package me.hatter.tests.xmpp;

import java.util.Arrays;

import me.hatter.tools.commons.log.LogUtil;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import com.alibaba.fastjson.JSON;

public class DefaultMessageListener implements MessageListener {

    public void processMessage(Chat chat, Message message) {

        LogUtil.info("Received message: "
                     + Arrays.<Object> asList(message.getType(), message.getFrom(), message.getSubject(),
                                              message.getBody()));
        LogUtil.info("Detail message: \n  " + JSON.toJSONString(message));
        if (message.getType() == Type.error) {
            return;
        }
        if (message.getBody() != null) {
            try {
                chat.sendMessage("You wrote: " + message.getBody());
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
    }
}
