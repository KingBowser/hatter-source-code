package me.hatter.tools.commons.net.mail.header;

import java.util.List;

public class Mail {

    private String         from;
    private String         sender;
    private String         returnPath;
    private String         to;
    private String         senderIp;
    private String         messageId;
    private String         authenticationResults;
    private String         subject;
    private String         mailer;
    private List<Received> receivedList;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReturnPath() {
        return returnPath;
    }

    public void setReturnPath(String returnPath) {
        this.returnPath = returnPath;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSenderIp() {
        return senderIp;
    }

    public void setSenderIp(String senderIp) {
        this.senderIp = senderIp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAuthenticationResults() {
        return authenticationResults;
    }

    public void setAuthenticationResults(String authenticationResults) {
        this.authenticationResults = authenticationResults;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMailer() {
        return mailer;
    }

    public void setMailer(String mailer) {
        this.mailer = mailer;
    }

    public List<Received> getReceivedList() {
        return receivedList;
    }

    public void setReceivedList(List<Received> receivedList) {
        this.receivedList = receivedList;
    }
}
