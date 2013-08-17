package me.hatter.tests.xmpp;

import me.hatter.tools.commons.log.LogUtil;

import org.jivesoftware.smack.ConnectionListener;

public class DefaultConnectionListener implements ConnectionListener {

    public void reconnectionSuccessful() {
        LogUtil.info("DefaultConnectionListener.reconnectionSuccessful");
    }

    public void reconnectionFailed(Exception exception) {
        LogUtil.error("DefaultConnectionListener.reconnectionFailed", exception);
    }

    public void reconnectingIn(int seconds) {
        LogUtil.info("DefaultConnectionListener.reconnectingIn:" + seconds);
    }

    public void connectionClosedOnError(Exception exception) {
        LogUtil.error("DefaultConnectionListener.connectionClosedOnError", exception);
    }

    public void connectionClosed() {
        LogUtil.info("DefaultConnectionListener.connectionClosed");
    }
}
