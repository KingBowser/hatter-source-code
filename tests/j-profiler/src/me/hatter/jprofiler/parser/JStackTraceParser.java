package me.hatter.jprofiler.parser;

import java.util.ArrayList;
import java.util.List;

import me.hatter.jprofiler.object.JStackTrace;

public class JStackTraceParser {

    public static JStackTrace parse(List<String> lines) {
        if ((lines != null) && (lines.size() <= 2)) {
            return null;
        }
        JStackTrace jStackTrace = new JStackTrace();
        jStackTrace.setStackTraceList(new ArrayList<String>());
        for (int i = 2; i < lines.size(); i++) {
            jStackTrace.getStackTraceList().add(lines.get(i).trim());
        }
        return jStackTrace;
    }
}
