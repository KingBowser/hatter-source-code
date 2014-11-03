package me.hatter.tools.histoana;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoParser {

    // num #instances #bytes class name
    // group1: count
    // group2: size
    // group3 name
    public static final Pattern P_LINE = Pattern.compile("\\s*\\d+:\\s+(\\d+)\\s+(\\d+)\\s+(.*)");

    public static ClassCountSizeMap parseHisto(String histo) {
        try {
            ClassCountSizeMap ccsm = new ClassCountSizeMap();
            BufferedReader br = new BufferedReader(new StringReader(histo));
            for (String line; ((line = br.readLine()) != null);) {
                ClassCountSize ccs = HistoParser.parseLine(line);
                if (ccs != null) {
                    ccsm.put(ccs.name, ccs);
                }
            }
            br.close();
            return ccsm;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ClassCountSize parseLine(String line) {
        if (line == null) {
            return null;
        }
        Matcher m = P_LINE.matcher(line);
        if (!m.matches()) {
            return null;
        }
        ClassCountSize ccs = new ClassCountSize();
        ccs.count = Long.parseLong(m.group(1));
        ccs.size = Long.parseLong(m.group(2));
        ccs.name = m.group(3);
        return ccs;
    }
}
