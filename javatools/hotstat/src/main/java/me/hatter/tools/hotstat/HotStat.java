package me.hatter.tools.hotstat;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.color.Color;
import me.hatter.tools.commons.color.Font;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.string.StringUtil;

public class HotStat {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        UnixArgsutil.parseGlobalArgs(args);
        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
            printJps();
            System.exit(0);
        }
        int pid = Integer.parseInt(UnixArgsutil.ARGS.args()[0]);
        int interval = 0;
        int count = Integer.MAX_VALUE;
        if (UnixArgsutil.ARGS.args().length > 1) {
            interval = Integer.parseInt(UnixArgsutil.ARGS.args()[1]);
            if (UnixArgsutil.ARGS.args().length > 2) {
                count = Integer.parseInt(UnixArgsutil.ARGS.args()[2]);
            }
        }
        Pattern filterPattern = null;
        if (UnixArgsutil.ARGS.kvalue("filter") != null) {
            filterPattern = Pattern.compile(UnixArgsutil.ARGS.kvalue("filter"));
        }

        boolean isColor = UnixArgsutil.ARGS.flags().contains("color");
        Font upFont = Font.createFont(isColor ? Color.getColor(101) : null);
        Font dnFont = Font.createFont(isColor ? Color.getColor(102) : null);

        int loopCount = (interval <= 0) ? 1 : count;
        Map<String, String> lastMap = null;
        for (int i = 0; i < loopCount; i++) {
            try {
                if (i > 0) {
                    Thread.sleep(interval);
                }
                System.out.println("---- hotstat diff ----");
                Map<String, String> map = HotSpotProcessUtil.listVMMonitor(pid);
                if (lastMap == null) {
                    for (String k : map.keySet()) {
                        if (accept(filterPattern, k)) {
                            System.out.println(StringUtil.paddingSpaceRight(k, 40) + " = " + map.get(k));
                        }
                    }
                } else {
                    Map<String, String> diffMap = new LinkedHashMap<String, String>();
                    for (String k : map.keySet()) {
                        if (!StringUtil.equals(map.get(k), lastMap.get(k))) {
                            diffMap.put(k, map.get(k));
                        }
                    }
                    if (diffMap.size() == 0) {
                        System.out.println("== no diff found ==");
                    } else {
                        for (String k : diffMap.keySet()) {
                            if (accept(filterPattern, k)) {
                                System.out.println(StringUtil.paddingSpaceRight(k, 40) + " = " + "[" + lastMap.get(k)
                                                   + " >>>> " + diffMap.get(k)
                                                   + diff(lastMap.get(k), diffMap.get(k), upFont, dnFont) + "]");
                            }
                        }
                    }
                }
                lastMap = map;
                System.out.println();
            } catch (Exception e) {
                LogUtil.error("Monitor vm failed.", e);
                System.exit(0);
            }
        }
    }

    public static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar hotstatall.jar [options] [<PID> [<interval> [<count>]]]");
        System.out.println("    -filter <regex filter expression>    regex filter expression");
        System.out.println("    -show <key name>                     contains filter expression");
        System.out.println("    --color                              show color");
    }

    public static boolean accept(Pattern p, String k) {
        if (p != null) {
            return p.matcher(k).matches();
        }
        if (UnixArgsutil.ARGS.keys().contains("show")) {
            return k.toLowerCase().contains(UnixArgsutil.ARGS.kvalue("show").toLowerCase());
        }
        return true;
    }

    public static void printJps() {
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
    }

    public static String diff(String ol, String ne, Font upFont, Font dnFont) {
        try {
            long vo = Long.parseLong(ol);
            long vn = Long.parseLong(ne);
            long di = vn - vo;
            Font font = Font.createFont(null);
            if (di > 0) {
                font = upFont;
            } else {
                font = dnFont;
            }
            DecimalFormat format = new DecimalFormat("#,###,###,##0");
            return " " + font.display("(" + ((di >= 0) ? "+" : StringUtil.EMPTY) + format.format(di) + ")");
        } catch (Exception e) {
            return StringUtil.EMPTY;
        }
    }
}
