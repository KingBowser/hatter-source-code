package me.hatter.tools.xmpprobots.biz;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;

public class BusinessMessage {

    private Chat     chat;
    private Message  message;
    private String   namespace;
    private String   cmd;
    private String[] args;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "BusinessMessage [chat=" + chat + ", message=" + message + "]";
    }
}
