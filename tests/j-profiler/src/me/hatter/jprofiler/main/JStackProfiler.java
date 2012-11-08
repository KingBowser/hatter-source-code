package me.hatter.jprofiler.main;

import java.io.File;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import me.hatter.jprofiler.object.JFullStack;
import me.hatter.jprofiler.object.JStack;
import me.hatter.jprofiler.object.JStackMap;
import me.hatter.jprofiler.object.JStackTidScore;
import me.hatter.jprofiler.parser.JFullStackParser;
import me.hatter.jprofiler.util.FileUtil;
import me.hatter.jprofiler.util.ListUtil;
import me.hatter.jprofiler.util.StringUtil;

public class JStackProfiler {

    public static void main(String[] args) {
        try {
            internal_main(args);
        } catch (Exception e) {
            System.out.println("[ERROR] Error occured in parse jstack file(s).");
            e.printStackTrace();
            System.out.println("[ANALYSIS FAILED]");
        }
    }

    public static void internal_main(String[] args) {
        if ((args == null) || (args.length == 0)) {
            System.out.println("Usage:");
            System.out.println("    jprofiler PID");
            System.out.println("");
            return;
        }
        if (!Pattern.compile("\\d+").matcher(args[0]).matches()) {
            System.out.println("[ERROR] PID should be number.");
            return;
        }
        String dir = System.getProperty("user.dir");
        List<File> files = new ArrayList<File>(FileUtil.listFiles(dir, "jstack", args[0]));
        if (files.size() == 0) {
            System.out.println("[ERROR] Cannot find PID stack file(s).");
            return;
        }
        Collections.sort(files, new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                String f1 = o1.getName();
                String f2 = o2.getName();
                int lDot1 = f1.lastIndexOf('.');
                int lDot2 = f2.lastIndexOf('.');
                return Integer.parseInt(f1.substring(lDot1 + 1)) - Integer.parseInt(f2.substring(lDot2 + 1));
            }
        });
        List<JFullStack> jFullStackList = new ArrayList<JFullStack>();
        for (File file : files) {
            System.out.println("[INFO] Parsing file: " + file);
            List<List<String>> linesList = StringUtil.parseToListByEmptyLine(FileUtil.readFile(file));
            JFullStack jFullStack = JFullStackParser.parse(linesList);
            jFullStackList.add(jFullStack);
        }

        List<JStackMap> jStackMapList = new ArrayList<JStackMap>();
        for (JFullStack jFullStack : jFullStackList) {
            filterJFullStack(jFullStack);
            jStackMapList.add(jStackToMap(jFullStack.getjStackList()));
        }

        Map<String, Integer> scoreMap = new HashMap<String, Integer>();
        JStackMap lastJStackMap = jStackMapList.get(jStackMapList.size() - 1);
        for (JStack jStack : lastJStackMap.values()) {
            scoreMap.put(jStack.getTid(), caculateScore(jStack, jStackMapList));
        }
        List<JStackTidScore> tidScoreList = new ArrayList<JStackTidScore>();
        for (Entry<String, Integer> entry : scoreMap.entrySet()) {
            JStackTidScore tidScore = new JStackTidScore();
            tidScore.setTid(entry.getKey());
            tidScore.setScore(entry.getValue());
            tidScoreList.add(tidScore);
        }
        Collections.sort(tidScoreList, new Comparator<JStackTidScore>() {

            @Override
            public int compare(JStackTidScore o1, JStackTidScore o2) {
                return (-(o1.getScore() - o2.getScore()));
            }
        });

        System.out.println("Analisys finished.");
        for (JStackTidScore tidScore : tidScoreList) {
            System.out.println();
            System.out.println(lastJStackMap.get(tidScore.getTid()).dumpJStack());
        }
        System.out.println("[ANALYSIS SUCCESS]");
    }

    private static Integer caculateScore(JStack jStack, List<JStackMap> jStackMapList) {
        int score = jStack.isDeamon() ? 10000 : 100000000;
        JStack lastJStack = jStack;
        for (int i = (jStackMapList.size() - 2); i >= 0; i--) {
            JStackMap jStackMap = jStackMapList.get(i);
            JStack currentJStack = jStackMap.get(lastJStack.getTid());
            if (currentJStack == null) {
                // has null stack trace
                score -= 1000;
            } else {
                // contains "com.alibaba"
                if (currentJStack.getStackTrace().containsComAlibaba()) {
                    score += 1000;
                }
                // add match count
                if ((lastJStack.getStackTrace() != null) && (currentJStack.getStackTrace()) != null) {
                    score += 10 * lastJStack.getStackTrace().matchsCount(currentJStack.getStackTrace());
                }
                lastJStack = currentJStack;
            }
        }
        return Integer.valueOf(score);
    }

    private static JStackMap jStackToMap(List<JStack> jStacks) {
        JStackMap jStackMap = new JStackMap();
        for (JStack jStack : jStacks) {
            jStackMap.put(jStack.getTid(), jStack);
        }
        return jStackMap;
    }

    private static void filterJFullStack(JFullStack jFullStack) {
        jFullStack.setjStackList(ListUtil.filter(jFullStack.getjStackList(), new ListUtil.Filter<JStack>() {

            @Override
            public boolean accept(JStack object) {
                if (State.RUNNABLE != object.getState()) {
                    return false;
                }
                if (object.getStackTrace() == null) {
                    // System.out.println("stack trace is null");
                    return false;
                }
                if (object.getStackTrace().getStackTraceList() == null) {
                    // System.out.println("stack trace list is null");
                    return false;
                }
                if (object.getStackTrace().getStackTraceList().size() == 0) {
                    // System.out.println("stack trace list is zero");
                    return false;
                }

                return true;
            }
        }));
    }
}
