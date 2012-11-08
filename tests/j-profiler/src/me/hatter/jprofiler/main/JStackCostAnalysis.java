package me.hatter.jprofiler.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import me.hatter.jprofiler.object.JFullStack;
import me.hatter.jprofiler.object.JStack;
import me.hatter.jprofiler.parser.JFullStackParser;
import me.hatter.jprofiler.util.FileUtil;
import me.hatter.jprofiler.util.StringUtil;

public class JStackCostAnalysis {

    private static String[] TRANS = new String[] { "update", "modify", "insert", "save", "create", "delete", "remove",
            "move", "post", "repost", "disable", "reorder", "recover", "repost", "freeze", "unfreeze", "add",
            "replace", "register", "bind", "newTransactionWrapper" };

    public static void main(String[] args) {
        File dd = new File("/Users/hatterjiang/temp/allstacks");
        File[] dds = dd.listFiles();

        Map<String, AtomicInteger> tidStacksMap = new HashMap<String, AtomicInteger>();
        for (File d : dds) {
            File[] files = d.listFiles();
            for (File f : files) {
                List<List<String>> linesList = StringUtil.parseToListByEmptyLine(FileUtil.readFile(f));
                JFullStack jFullStack = JFullStackParser.parse(linesList);
                JSTACK: for (JStack jStack : jFullStack.getjStackList()) {
                    if ((jStack.getStackTrace() == null) || (jStack.getStackTrace().getStackTraceList() == null)) {
                        continue;
                    }
                    String tid = jStack.getTid();
                    List<String> stacks = jStack.getStackTrace().getStackTraceList();
                    if (stacks.size() < 20) {
                        continue;
                    }
                    boolean hasAlibaba = false;
                    for (String s : stacks) {
                        if (s.contains("com.alibaba")) {
                            hasAlibaba = true;
                            break;
                        }
                        if (s.contains("org.apache.jk.common.ChannelSocket")) {
                            continue JSTACK;
                        }
                    }
                    if (!hasAlibaba) {
                        continue;
                    }

                    StringBuilder sb = new StringBuilder(200);
                    for (int i = 0; i < stacks.size(); i++) {
                        sb.append("  " + stacks.get(i) + "\n");
                    }
                    String key = tid + "\n" + sb.toString();
                    AtomicInteger ai = tidStacksMap.get(key);
                    if (ai == null) {
                        ai = new AtomicInteger(0);
                        tidStacksMap.put(key, ai);
                    }
                    ai.incrementAndGet();
                }
            }
        }
        List<Entry<String, AtomicInteger>> el = new ArrayList<Map.Entry<String, AtomicInteger>>(tidStacksMap.entrySet());
        Collections.sort(el, new Comparator<Entry<String, AtomicInteger>>() {

            @Override
            public int compare(Entry<String, AtomicInteger> o1, Entry<String, AtomicInteger> o2) {
                return o1.getValue().intValue() - o2.getValue().intValue();
            }
        });
        for (Entry<String, AtomicInteger> e : el) {
            if (e.getValue().intValue() == 1) continue;
            System.out.println("----------------------------------------------------------------");
            System.out.println("[" + e.getValue().intValue() + "]  " + e.getKey());
        }
    }
}
