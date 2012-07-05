package me.hatter.tools.hotstat;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotProcessVM;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.management.ManagementUtil;
import me.hatter.tools.commons.string.StringUtil;

public class HotStat {

    public static void main(String[] args) {
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
                        System.out.println(StringUtil.paddingSpaceRight(k, 40) + " = " + map.get(k));
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
                            System.out.println(StringUtil.paddingSpaceRight(k, 40) + " = " + "[" + lastMap.get(k)
                                               + " >>>> " + diffMap.get(k) + diff(lastMap.get(k), diffMap.get(k)) + "]");
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
    }

    public static void printJps() {
        System.out.println();
        List<HotSpotProcessVM> vms = HotSpotProcessUtil.listVMs();
        System.out.println(StringUtil.paddingSpaceRight("PID", 10) + StringUtil.paddingSpaceRight("ATTACH ABLE", 13)
                           + "CLASS");
        String currentPid = ManagementUtil.getCurrentVMPid();
        for (HotSpotProcessVM vm : vms) {
            if (currentPid.equals(String.valueOf(vm.getPid()))) {
                continue;
            }
            System.out.println(StringUtil.paddingSpaceRight(String.valueOf(vm.getPid()), 10)
                               + StringUtil.paddingSpaceRight(String.valueOf(vm.isAttachAble()), 13)
                               + vm.getFullClassName());
        }
    }

    public static String diff(String ol, String ne) {
        try {
            long vo = Long.parseLong(ol);
            long vn = Long.parseLong(ne);
            return " (DIFF " + (vn - vo) + ")";
        } catch (Exception e) {
            return StringUtil.EMPTY;
        }
    }
}
