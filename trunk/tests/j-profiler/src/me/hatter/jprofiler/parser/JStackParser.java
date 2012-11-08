package me.hatter.jprofiler.parser;

import java.lang.Thread.State;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.hatter.jprofiler.exception.ParseException;
import me.hatter.jprofiler.object.JStack;

public class JStackParser {

    private static Pattern DATE_PATTERN         = Pattern.compile("\\d{4}(\\-\\d{2}){2}\\s+\\d{2}(:\\d{2}){2}");
    private static Pattern STACK_PATTERN        = Pattern.compile("(\\\")([^\\\"]+)\\1(\\s+(daemon))?\\s+prio=(\\d+)\\s+tid=([\\da-fx]+)\\s+nid=([\\da-fx]+)\\s+(.*)");
    private static Pattern STATE_STATUS_PATTERN = Pattern.compile("\\s+java.lang.Thread.State:\\s+([\\w_]+)\\s*(\\([^)]+\\))?");

    public static JStack parse(List<String> lines) {
        if (lines.size() == 0) {
            return null;
        }
        if ((lines.size() >= 1) && (lines.get(0).trim().equals("Locked ownable synchronizers:"))) {
            return null;
        }
        String firstLine = lines.get(0);
        String secondLine = (lines.size() >= 2) ? lines.get(1) : null;

        if (firstLine.startsWith("JNI global references:") && (lines.size() == 1)) {
            return null;
        }

        if (DATE_PATTERN.matcher(firstLine).matches() && (secondLine != null)
            && (secondLine.trim().startsWith("Full thread dump "))) {
            // ignore "Date & Full thread dump "
            return null;
        }

        Matcher m = STACK_PATTERN.matcher(firstLine);
        if (!m.matches()) {
            throw new ParseException("Cannot parse stack: " + firstLine + "; " + lines);
        }
        String name = m.group(2);
        String deamon = m.group(4);
        String priority = m.group(5);
        String tid = m.group(6);
        String nid = m.group(7);
        String others = m.group(8);

        JStack jStack = new JStack();
        jStack.setName(name);
        jStack.setDeamon("daemon".equalsIgnoreCase(deamon));
        jStack.setPriority(priority);
        jStack.setTid(tid);
        jStack.setNid(nid);
        jStack.setOthers(others);

        if (secondLine != null) {
            if (secondLine.trim().startsWith(State.class.getName().replace('$', '.'))) {
                Matcher ms = STATE_STATUS_PATTERN.matcher(secondLine);
                if (!ms.matches()) {
                    throw new ParseException("Caonnt parse state: " + secondLine);
                }
                jStack.setState(State.valueOf(ms.group(1)));
            } else {
                if (secondLine.startsWith("JNI ") && (lines.size() == 2)) {
                    // ignore this line
                } else if (STACK_PATTERN.matcher(secondLine).matches() && (lines.size() == 2)) {
                    // ignore this line too
                } else {
                    throw new RuntimeException("Unknow jstack file format: " + secondLine);
                }
            }
        }

        jStack.setStackTrace(JStackTraceParser.parse(lines));

        return jStack;
    }
}
