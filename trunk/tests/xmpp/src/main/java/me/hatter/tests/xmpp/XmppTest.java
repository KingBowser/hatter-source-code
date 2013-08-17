package me.hatter.tests.xmpp;

import java.io.File;
import java.util.Arrays;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.log.LogUtil;
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

    private static final String SERVER = "talk.l.google.com";

    private static String       userName;
    private static String       password;
    static {
        String xmpp = StringUtil.trim(FileUtil.readFileToString(new File(Environment.USER_HOME, "xmpp.txt")));
        userName = StringUtil.substringBefore(xmpp, "|");
        password = StringUtil.substringAfter(xmpp, "|");
    }

    public static void main(String[] args) throws Exception {

        Connection connection = new XMPPConnection(SERVER);
        connection.connect();
        LogUtil.info("Summary: "
                     + Arrays.asList(connection.isConnected(), connection.isAuthenticated(), connection.isAnonymous()));
        LogUtil.info("Host and port: " + Arrays.<Object> asList(connection.getHost(), connection.getPort()));
        LogUtil.info("Is secure connection: " + connection.isSecureConnection());
        LogUtil.info("Is using compression: " + connection.isUsingCompression());
        LogUtil.info("Service capsnode/name: "
                     + Arrays.asList(connection.getServiceCapsNode(), connection.getServiceName()));

        connection.login(userName, password);
        LogUtil.info("Summary2: "
                     + Arrays.asList(connection.isConnected(), connection.isAuthenticated(), connection.isAnonymous()));

        // LogUtil.info("Supports account creation: " + connection.getAccountManager().supportsAccountCreation());

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
        LogUtil.info("Total: " + roster.getEntryCount());
        for (RosterEntry entry : roster.getEntries()) {
            LogUtil.info("    " + Arrays.asList(entry.getType(), entry.getUser(), entry.getName(), entry.getStatus()));
        }
        LogUtil.info("Has no group entry count: " + roster.getUnfiledEntryCount());

        Thread.sleep(10000000);
    }
}
