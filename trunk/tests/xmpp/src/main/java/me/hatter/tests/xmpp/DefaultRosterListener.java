package me.hatter.tests.xmpp;

import java.util.Arrays;
import java.util.Collection;

import me.hatter.tools.commons.log.LogUtil;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;


public class DefaultRosterListener implements RosterListener {

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
}
