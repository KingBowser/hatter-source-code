package me.hatter.tools.hostsmanager.hostsfilter.impl;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.hostsmanager.hosts.Line;
import me.hatter.tools.hostsmanager.hostsfilter.HostsFilter;

public class EmptyGroupFilter implements HostsFilter {

    public List<String> filter(List<String> lines) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            String l = lines.get(i);
            String n = null;
            if ((i + 1) < lines.size()) {
                n = lines.get(i + 1);
            }
            if (l.startsWith(Line.GROUP_START) && (n != null) && n.startsWith(Line.GROUP_END)) {
                i++;
                continue;
            }
            result.add(l);
        }
        return result;
    }
}
