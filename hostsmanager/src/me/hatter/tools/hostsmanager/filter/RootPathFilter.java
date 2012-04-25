package me.hatter.tools.hostsmanager.filter;

import me.hatter.tools.resourceproxy.jsspserver.filter.impl.AbstractRootFilter;

public class RootPathFilter extends AbstractRootFilter {

    @Override
    protected String homePath() {
        return "index.jssp?jsspaction=hostsmanager.Index";
    }
}
