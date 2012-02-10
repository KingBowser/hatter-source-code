package me.hatter.tool;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgsUtil {

    public static String[] mappingArgs(String[] args, Object bean) {
        Map<String, String> refMap = new HashMap<String, String>();
        String[] pargs = processArgs(args, refMap);
        mappingToBean(refMap, bean);
        return pargs;
    }

    private static void mappingToBean(Map<String, String> map, Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if ((f.getType() == boolean.class) || (f.getType() == Boolean.class)) {
                    if (map.containsKey(f.getName())) {
                        f.set(bean, true);
                    }
                } else if (f.getType() == String.class) {
                    if (map.containsKey(f.getName())) {
                        f.set(bean, map.get(f.getName()));
                    }
                } else {
                    throw new RuntimeException("unknow type: " + f.getType().getName());
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Error in set arg flags: " + e.getMessage());
                if (System.getProperties().containsKey("program.trace")) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String[] processArgs(String[] args, Map<String, String> refMap) {
        List<String> argList = new ArrayList<String>();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("-") && arg.length() > 1) {
                    String narg = arg.substring(1);
                    int indexOfSp = narg.indexOf('/');
                    if (indexOfSp >= 0) {
                        refMap.put(narg.substring(0, indexOfSp).toLowerCase(), narg.substring(indexOfSp + 1));
                    } else {
                        refMap.put(narg.toLowerCase(), null);
                    }
                } else {
                    argList.add(arg);
                }
            }
        }
        return argList.toArray(new String[0]);
    }
}
