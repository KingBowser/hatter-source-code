package me.hatter.tools.hostsmanager.hostsfilter;

import java.util.List;

public class HostsFilters {

    public static List<String> filter(List<HostsFilter> filters, List<String> lines) {
        for (HostsFilter filter : filters) {
            lines = filter.filter(lines);
        }
        return lines;
    }
}
