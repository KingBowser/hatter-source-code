package me.hatter.tools.hostsmanager.hostsfilter.impl;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.hostsmanager.hostsfilter.HostsFilter;

public class CommentFilter implements HostsFilter {

    public List<String> filter(List<String> lines) {
        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            if (line.trim().startsWith("#") && (!line.trim().startsWith("#GROUP"))) {
                result.add(line);
            }
        }
        return result;
    }
}
