package me.hatter.tests.xmpp;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.string.StringUtil;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

// http://julipedia.meroh.net/2012/07/getting-rid-of-publictalkgooglecom.html
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
        connection.getRoster().setSubscriptionMode(SubscriptionMode.accept_all);
        connection.getRoster().addRosterListener(new DefaultRosterListener());
        connection.addConnectionListener(new DefaultConnectionListener());
        connection.getChatManager().addChatListener(new ChatManagerListener() {

            public void chatCreated(Chat chat, boolean createLocally) {
                if (!createLocally) {
                    chat.addMessageListener(new DefaultMessageListener());
                }
            }
        });
        Roster roster = connection.getRoster();
        // roster.createEntry("jht5945@gmail.com", "Hatter Jinag@G", null);
        System.out.println("Total: " + roster.getEntryCount());
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            System.out.println("    "
                               + Arrays.asList(entry.getType(), entry.getUser(), entry.getName(), entry.getStatus()));
        }
        Thread.sleep(10000000);
    }
}
