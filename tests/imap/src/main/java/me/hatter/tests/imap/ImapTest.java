package me.hatter.tests.imap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import me.hatter.tools.commons.log.LogUtil;

// http://namheo.wordpress.com/2010/04/26/smtps-imaps-in-java-android/
public class ImapTest {

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        // IMAPS protocol
        props.setProperty("mail.store.protocol", "imaps");
        // Set host address
        props.setProperty("mail.imaps.host", "imaps.gmail.com");
        // Set specified port
        props.setProperty("mail.imaps.port", "993");
        // Using SSL
        props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imaps.socketFactory.fallback", "false");
        // Setting IMAP session
        Session imapSession = Session.getInstance(props);

        // -----------------------------------------------------------------------------------

        Store store = imapSession.getStore("imaps");
        // Connect to server by sending username and password.
        // Example mailServer = imap.gmail.com, username = abc, password = abc
        store.connect("imap.gmail.com", EmailAccount.USERNAME + "@gmail.com", EmailAccount.PASSWORD);
        // Get all mails in Inbox Forlder
        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_WRITE);

        inbox.addMessageChangedListener(new MessageChangedListener() {

            public void messageChanged(MessageChangedEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
        inbox.addMessageCountListener(new MessageCountListener() {

            public void messagesRemoved(MessageCountEvent event) {
                Message[] messages = event.getMessages();
                List<String> messageList = new ArrayList<String>();
                if (messages != null) {
                    for (Message m : messages) {
                        try {
                            messageList.add(m.getSubject());
                        } catch (MessagingException e) {
                            messageList.add(m.toString());
                        }
                    }
                }
                LogUtil.info("IMAP: messagesRemoved: " + messageList);
            }

            public void messagesAdded(MessageCountEvent event) {
                Message[] messages = event.getMessages();
                List<String> messageList = new ArrayList<String>();
                if (messages != null) {
                    for (Message m : messages) {
                        try {
                            messageList.add(m.getSubject());
                        } catch (MessagingException e) {
                            messageList.add(m.toString());
                        }
                    }
                }
                LogUtil.info("IMAP: messagesAdded: " + messageList);
            }
        });

        inbox.setSubscribed(true);

        // Return result to array of message
        Message[] result = inbox.getMessages();
        System.out.println(result.length);
        // for (Message message : result) {
        // System.out.println(message.getSubject() + Arrays.asList(message.getFlags().getUserFlags()));
        // for (Flag flag : message.getFlags().getSystemFlags()) {
        // System.out.println("    "
        // + Arrays.asList(flag.equals(Flag.ANSWERED), flag.equals(Flag.DELETED),
        // flag.equals(Flag.DRAFT), flag.equals(Flag.FLAGGED),
        // flag.equals(Flag.RECENT), flag.equals(Flag.SEEN),
        // flag.equals(Flag.USER)));
        // }
        // message.setFlag(Flag.SEEN, true);
        // }
        Thread.sleep(100000000L);
    }
}
