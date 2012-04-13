package me.hatter.tools.hostsmanager.hostsfilter.impl;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.hostsmanager.hostsfilter.HostsFilter;

public class DefaultFilter implements HostsFilter {

    public List<String> filter(List<String> lines) {
        return new ArrayList<String>(lines);
    }
}
