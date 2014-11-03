package me.hatter.tools.jflag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.hatter.tools.commons.io.StringBufferedReader;

public class FlagParserUtil {

    private static Pattern PARSER_PATTERN = Pattern.compile("([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)");

    public static List<Flag> parseFlagList(String str) {
        List<Flag> flagList = new ArrayList<Flag>();
        StringBufferedReader reader = new StringBufferedReader(str);
        try {
            for (String line; ((line = reader.readLine()) != null);) {
                Matcher m = PARSER_PATTERN.matcher(line);
                if (!m.matches()) {
                    throw new RuntimeException("Format error: " + line);
                }
                String name = m.group(1);
                FlagRuntimeType runtime = FlagRuntimeType.valueOf("_" + m.group(3));
                FlagValueType type = FlagValueType.valueOf("_" + m.group(2));
                Flag flag = new Flag(name, runtime, type);
                flagList.add(flag);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return flagList;
    }
}
