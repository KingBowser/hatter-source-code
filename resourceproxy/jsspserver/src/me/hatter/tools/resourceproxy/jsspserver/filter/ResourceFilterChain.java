package me.hatter.tools.resourceproxy.jsspserver.filter;

public interface ResourceFilterChain {

    ResourceFilter next();
}
