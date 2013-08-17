package me.hatter.tests.xmpp;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.string.StringUtil;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

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
        connection.getRoster().addRosterListener(new RosterListener() {

            public void presenceChanged(Presence presence) {
                LogUtil.info("presenceChanged:"
                             + Arrays.<Object> asList(presence.getFrom(), presence.getTo(), presence.getMode(),
                                                      presence.getStatus(), presence.getType()));
            }

            public void entriesUpdated(Collection<String> entries) {
                LogUtil.info("entriesUpdated:" + entries);
            }

            public void entriesDeleted(Collection<String> entries) {
                LogUtil.info("entriesDeleted:" + entries);
            }

            public void entriesAdded(Collection<String> entries) {
                LogUtil.info("entriesAdded:" + entries);
            }
        });
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
        connection.getChatManager().addChatListener(new ChatManagerListener() {

            public void chatCreated(Chat chat, boolean createLocally) {
                if (!createLocally) {
                    chat.addMessageListener(new DefaultMessageListener());
                }
            }
        });
        Roster roster = connection.getRoster();
        roster.setSubscriptionMode(SubscriptionMode.accept_all);
        // roster.createEntry("jht5945@gmail.com", "Hatter Jinag@G", null);
        System.out.println("Total: " + roster.getEntryCount());
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            System.out.println("    "
                               + Arrays.asList(entry.getType(), entry.getUser(), entry.getName(), entry.getStatus()));
        }
        // Chat chat = connection.getChatManager().createChat("jht5945@gmail.com", new DefaultMessageListener());
        // chat.sendMessage("test message !");
        Thread.sleep(10000000);
    }
}
