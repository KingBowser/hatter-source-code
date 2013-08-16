package me.hatter.tests.imap;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SmtpTest {

    public static void main(String[] args) throws Exception {
        EmailAccount account = new EmailAccount(EmailAccount.USERNAME, EmailAccount.PASSWORD, "gmail.com");
        // Create an authetication email
        EmailAuthenticator authenticator = new EmailAuthenticator(account);
        // Create a property for smtp sesion
        Properties props = new Properties();
        // Setting smtps protocol
        props.setProperty("mail.transport.protocol", "smtps");
        // Setting stmpHost. Example: smtpHost = smtp.gmail.com
        props.setProperty("mail.host", "smtp.gmail.com");
        // Setting specified port
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        // Setting using SSL
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        // Enable authentication
        props.put("mail.smtp.auth", "true");
        // Setting smtp session
        Session smtpSession = Session.getDefaultInstance(props, authenticator);

        // -----------------------------------------------------------------------------------

        // Example: sender=nam.tran.flash@gmail.com recipients = nam.tran@glandoresystems.com
        // body= the body of message, subject= the title of message
        MimeMessage message = new MimeMessage(smtpSession);
        // Create handler data to contain the body of message in bytes using ByteArrayDataSource
        DataHandler handler = new DataHandler(new ByteArrayDataSource("hello world".getBytes(), "text/plain"));
        // Put information in message
        message.setSender(new InternetAddress(EmailAccount.USERNAME + "@gmail.com"));
        message.setSubject("hello");
        message.setDataHandler(handler);

        String recipients = "jht5945@gmail.com";
        if (recipients.indexOf(',') > 0) {// Check recipients to send many email address at the same time
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        } else {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
        }
        // Send email
        Transport.send(message);
    }
}
