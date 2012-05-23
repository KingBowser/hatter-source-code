package me.hatter.tools.registerclassmate.filter;

import me.hatter.tools.resourceproxy.jsspserver.filter.impl.AbstractRootFilter;

public class RootPathFilter extends AbstractRootFilter {

    @Override
    protected String homePath() {
        return "main.jssp?jsspaction=classes.Main";
    }
}
