package me.hatter.tests.xmpp;

import java.io.File;
import java.util.Arrays;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.string.StringUtil;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class XmppTest {

    private static final String TALK = "talk.l.google.com";

    private static String       userName;
    private static String       password;
    static {
        String xmpp = StringUtil.trim(FileUtil.readFileToString(new File(Environment.USER_HOME, "xmpp.txt")));
        userName = StringUtil.substringBefore(xmpp, "|");
        password = StringUtil.substringAfter(xmpp, "|");
    }

    public static void main(String[] args) throws Exception {

        // ConnectionConfiguration config = new ConnectionConfiguration(TALK);
        // config.setCompressionEnabled(true);
        // config.setSASLAuthenticationEnabled(true);
        // config.setSecurityMode(SecurityMode.enabled);

        Connection connection = new XMPPConnection(TALK);
        connection.connect();
        connection.login(userName, password);
        connection.addConnectionListener(new ConnectionListener() {

            public void reconnectionSuccessful() {
                LogUtil.info("reconnectionSuccessful");
            }

            public void reconnectionFailed(Exception exception) {
                LogUtil.error("reconnectionFailed", exception);
            }

            public void reconnectingIn(int seconds) {
                LogUtil.info("reconnectingIn:" + seconds);
            }

            public void connectionClosedOnError(Exception exception) {
                LogUtil.error("connectionClosedOnError", exception);
            }

            public void connectionClosed() {
                LogUtil.info("connectionClosed");
            }
        });
        Chat chat = connection.getChatManager().createChat("jht5945@gmail.com", new MessageListener() {

            public void processMessage(Chat chat, Message message) {
                System.out.println("Received message: "
                                   + Arrays.<Object> asList(message.getType(), message.getSubject(), message.getBody()));
                if (message.getBody() != null) {
                    try {
                        chat.sendMessage("You wrote: " + message.getBody());
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        chat.sendMessage("hello !!" + System.currentTimeMillis());

        Thread.sleep(100000L);
    }
}
