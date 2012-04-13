package me.hatter.tools.hostsmanager.hostsfilter;

import java.util.List;

public interface HostsFilter {

    List<String> filter(List<String> lines);
}
