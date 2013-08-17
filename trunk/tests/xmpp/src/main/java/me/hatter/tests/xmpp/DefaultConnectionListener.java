package me.hatter.tests.xmpp;

import me.hatter.tools.commons.log.LogUtil;

import org.jivesoftware.smack.ConnectionListener;

public class DefaultConnectionListener implements ConnectionListener {

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
}
