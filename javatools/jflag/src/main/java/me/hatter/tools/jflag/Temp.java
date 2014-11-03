package me.hatter.tools.jflag;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.hatter.tools.commons.io.FileBufferedReader;
import me.hatter.tools.commons.string.StringUtil;

public class Temp {

    public static void main(String[] args) throws Exception {
        File f = new File("/Users/hatterjiang/Code/hatter-source-code/jflag", "flags");
        Pattern p = Pattern.compile("([^\\(]+)\\(([a-z0-9_]+),\\s*([a-zA-Z0-9_]+),\\s*(.*)\\\\");
        Set<String> set = new HashSet<String>();
        FileBufferedReader r = new FileBufferedReader(f);
        for (String ln; ((ln = r.readLine()) != null);) {
            if (ln.trim().startsWith("/*")) continue;
            if (ln.trim().startsWith("//")) continue;
            if (ln.trim().isEmpty()) continue;
            Matcher m = p.matcher(ln.trim());
            if (!m.matches()) {
                System.out.println("ERROR" + ln);
                (new Exception()).printStackTrace();
                System.exit(0);
            }
            // System.out.println(ln);
            if (set.contains(m.group(3))) {
                System.out.println("EEEEEEEEE " + m.group(3));
                System.exit(0);
            }
            set.add(m.group(3));
            System.out.println(StringUtil.paddingSpaceRight(m.group(3), 80)
                               + StringUtil.paddingSpaceRight(m.group(2), 20) + m.group(1));// +
            // "    "
            // +
            // m.group(4));
        }
        System.out.println();
        System.out.println(set);
    }
}
