package me.hatter.tools.hostsmanager.hostsfilter.impl;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.hostsmanager.hosts.Line;
import me.hatter.tools.hostsmanager.hostsfilter.HostsFilter;

public class CommentFilter implements HostsFilter {

    public List<String> filter(List<String> lines) {
        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            String trimedLine = line.trim();
            if (trimedLine.startsWith(Line.COMMENT)) {
                if (trimedLine.startsWith(Line.GROUP)) {
                    result.add(line);
                }
            } else {
                result.add(line);
            }
        }
        return result;
    }
}
