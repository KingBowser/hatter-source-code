package me.hatter.tools.commons.log.analysis;

import java.util.ArrayList;
import java.util.List;

public class HttpLogUtil {

    public static List<String> parseLine(String line) {
        List<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ' ') {
                list.add(sb.toString().trim());
                sb.delete(0, sb.length());
            } else {
                if (c == '[') {
                    sb.append(c);
                    i++;
                    FLOOP: for (; i < line.length(); i++) {
                        char x = line.charAt(i);
                        sb.append(x);
                        if (x == ']') {
                            break FLOOP;
                        }
                    }
                } else if (c == '"') {
                    sb.append(c);
                    i++;
                    FLOOP: for (; i < line.length(); i++) {
                        char x = line.charAt(i);
                        sb.append(x);
                        if ((x == '"') && (line.charAt(i - 1) != '\\')) {
                            break FLOOP;
                        }
                    }
                } else {
                    sb.append(c);
                }
            }
        }
        if (sb.length() > 0) {
            list.add(sb.toString().trim());
        }
        return list;
    }
}
