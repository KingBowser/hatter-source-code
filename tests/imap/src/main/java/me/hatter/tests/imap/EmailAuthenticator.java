package me.hatter.tests.imap;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthenticator extends Authenticator {

    private EmailAccount account;

    public EmailAuthenticator(EmailAccount account) {
        super();
        this.account = account;
    }

    // Override getPasswordAuthentication to transfer emailAddress and password to get an authentication.
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(account.emailAddress, account.password);
    }
}
