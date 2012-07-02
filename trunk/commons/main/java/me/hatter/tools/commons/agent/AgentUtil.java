package me.hatter.tools.commons.agent;

import java.util.HashMap;
import java.util.Map;

public class AgentUtil {

    public static Map<String, String> parseAgentArgsMap(String str) {
        Map<String, String> map = new HashMap<String, String>();
        if (str != null) {
            String[] arr = str.split(",");
            for (String s : arr) {
                int indexOfEq = s.indexOf('=');
                if (indexOfEq == -1) {
                    map.put(s, "");
                } else if (indexOfEq > 0) {
                    map.put(s.substring(0, indexOfEq), s.substring(indexOfEq + 1));
                } else {
                    System.out.println("[WARN] parameter illegal: " + s);
                }
            }
        }
        return map;
    }
}
