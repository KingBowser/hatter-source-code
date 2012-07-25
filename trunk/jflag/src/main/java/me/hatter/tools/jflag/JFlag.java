package me.hatter.tools.jflag;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.jvm.HotSpotAttachTool;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.regex.RegexUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.flagagent.management.HotSpotFlagMXBean;
import sun.tools.attach.HotSpotVirtualMachine;

import com.sun.management.VMOption;

public class JFlag {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        UnixArgsutil.parseGlobalArgs(args);

        String flags = IOUtil.readResourceToString(JFlag.class, "flags.txt");
        List<Flag> flagList = FlagParserUtil.parseFlagList(flags);

        if (UnixArgsutil.ARGS.flags().contains("show-cust-flags") && (UnixArgsutil.ARGS.args().length == 0)) {
            cutsFlags();
        }
        if (UnixArgsutil.ARGS.flags().contains("show-remote-flags") && (UnixArgsutil.ARGS.args().length == 1)) {
            remoteFlags();
        }
        if (((UnixArgsutil.ARGS.kvalue("show") != null) || (UnixArgsutil.ARGS.kvalue("filter") != null))
            && (UnixArgsutil.ARGS.args().length == 0)) {
            flags(flagList);
        }
        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }
        String pid = UnixArgsutil.ARGS.args()[0];

        if (UnixArgsutil.ARGS.kvalue("flag") != null) {
            List<String> _flagList = UnixArgsutil.ARGS.kvalues("flag");
            for (String _flag : _flagList) {
                boolean isOn = _flag.startsWith("+");
                boolean isOff = _flag.startsWith("-");
                boolean isEq = _flag.contains("=");
                if (!(isOn || isOff || isEq)) {
                    LogUtil.error("Invalid flag args: " + _flag);
                } else {
                    String _flagValue = isEq ? _flag : _flag.substring(1);
                    String _flagName = _flagValue;
                    Boolean _isOn = null;
                    if (isOn || isOff) {
                        _isOn = Boolean.valueOf(isOn);
                    }
                    String _flagArgs = null;
                    if (isEq) {
                        _flagName = StringUtil.substringBefore(_flagValue, "=");
                        _flagArgs = StringUtil.substringAfter(_flagValue, "=");
                    }
                    LogUtil.info("Set flag " + _flagName + " -> " + _isOn + " @ " + _flagArgs);

                    String result = null;
                    if (_flagName.startsWith("~")) {
                        String value = (isEq) ? _flagArgs : (isOn ? "true" : "false");
                        HotSpotFlagMXBean flagMXBean = new RemoteFlagTool(UnixArgsutil.ARGS.args()[0]).getHotSpotFlagMXBean();
                        flagMXBean.setVMOption(_flagName.substring(1), value);
                    } else {
                        try {
                            JFlagCommand cmd = JFlagCommand.valueOf(_flagName);
                            result = cmd.getHandler().handle(cmd, _isOn, _flagArgs);
                        } catch (Exception e) {
                            String value = (isEq) ? _flagArgs : (isOn ? "1" : "0");
                            HotSpotAttachTool attach = new HotSpotAttachTool(pid);
                            attach.attach();
                            try {
                                HotSpotVirtualMachine hvm = (HotSpotVirtualMachine) attach.getVM();
                                InputStream isVal = hvm.setFlag(_flagName, value);
                                result = IOUtil.readToStringAndClose(isVal, "UTF-8");
                            } catch (IOException ex) {
                                LogUtil.error("", ex);
                            } finally {
                                attach.detach();
                            }
                        }
                    }
                    if ((result != null) && (!result.isEmpty())) {
                        LogUtil.info("Return with result: " + result);
                    }
                }
            }
        } else {
            getFlags(flagList, pid);
        }
    }

    private static void getFlags(List<Flag> flagList, String pid) {
        Set<String> ignoreSet = new HashSet<String>();
        ignoreSet.add("FLSLargestBlockCoalesceProximity");
        ignoreSet.add("CMSSmallCoalSurplusPercent");
        ignoreSet.add("CMSLargeCoalSurplusPercent");
        ignoreSet.add("CMSSmallSplitSurplusPercent");
        ignoreSet.add("CMSLargeSplitSurplusPercent");
        System.out.println("Default ignore: " + ignoreSet);

        HotSpotAttachTool attach = new HotSpotAttachTool(pid);
        attach.attach();
        try {
            HotSpotVirtualMachine hvm = (HotSpotVirtualMachine) attach.getVM();
            try {
                System.out.println(StringUtil.paddingSpaceRight("FlagName", 40) + " "
                                   + StringUtil.paddingSpaceRight("Type", 20)
                                   + StringUtil.paddingSpaceRight("Runtime", 20) + "Value");
                System.out.println(StringUtil.repeat("-", 91));
                for (Flag flag : flagList) {
                    if (ignoreSet.contains(flag.getName())) continue;
                    if (!filterFlag(flag)) continue;

                    String result = IOUtil.readToStringAndClose(hvm.printFlag(flag.getName()), "UTF-8").trim();
                    boolean notExists = result.toLowerCase().contains("no such flag");
                    if (notExists) {
                        if (UnixArgsutil.ARGS.flags().contains("show-not-exists")) {
                            System.out.println(result);
                        }
                    } else {
                        try {
                            String[] nameValue = parseFlag(result);
                            Flag f = getFlag(flagList, nameValue[0]);
                            System.out.println(StringUtil.paddingSpaceRight(nameValue[0], 40)
                                               + " "
                                               + StringUtil.paddingSpaceRight(((f == null) ? "unknow" : f.getType().getName()),
                                                                              20)
                                               + StringUtil.paddingSpaceRight(((f == null) ? "unknow" : f.getRuntime().getName()),
                                                                              20) + nameValue[1]);
                        } catch (Exception e) {
                            System.out.println(result + " (parse failed: " + e.getMessage() + ")");
                        }
                    }
                }
            } catch (IOException e) {
                LogUtil.error("", e);
            }
        } finally {
            attach.detach();
        }
    }

    private static Flag getFlag(List<Flag> flagList, String name) {
        for (Flag flag : flagList) {
            if (name.equals(flag.getName())) {
                return flag;
            }
        }
        return null;
    }

    private static String[] parseFlag(String _xxflag) {
        if ((_xxflag == null) || (_xxflag.length() == 0)) {
            return null;
        }
        _xxflag = (_xxflag.startsWith("-XX:")) ? _xxflag.substring("-XX:".length()) : _xxflag;
        boolean isOn = _xxflag.startsWith("+");
        boolean isOff = _xxflag.startsWith("-");
        boolean isEq = _xxflag.contains("=");

        String name = _xxflag;
        String value = null;
        if (isOn || isOff) {
            name = _xxflag.substring(1);
            value = isOn ? "true" : "false";
        } else if (isEq) {
            name = StringUtil.substringBefore(_xxflag, "=");
            value = StringUtil.substringAfter(_xxflag, "=");
        }

        return new String[] { name, value };
    }

    private static boolean filterName(String name) {
        if (name == null) {
            return false;
        }
        String show = UnixArgsutil.ARGS.kvalue("show");
        boolean showAll = ("ALL".equals(show) || (show == null));
        if (!(showAll || name.toLowerCase().contains(show.toLowerCase()))) {
            return false;
        }
        Pattern pattern = RegexUtil.createPattern(UnixArgsutil.ARGS.kvalue("filter"), true);
        if (pattern != null) {
            return pattern.matcher(name).matches();
        }
        return true;
    }

    private static boolean filterFlag(Flag flag) {
        if (flag == null) {
            return false;
        }
        {
            if (!filterName(flag.getName())) {
                return false;
            }
        }

        {
            String runt = UnixArgsutil.ARGS.kvalue("runtime");
            if ((runt == null) && (UnixArgsutil.ARGS.flags().contains("writable"))) {
                runt = FlagRuntimeType._product_rw.getName() + "," + FlagRuntimeType._manageable.getName();
            }
            boolean showAll = ("ALL".equals(runt) || (runt == null));
            Set<FlagRuntimeType> runtSet = new HashSet<FlagRuntimeType>();
            if (!showAll) {
                String[] runts = runt.split(",");
                for (String rt : runts) {
                    runtSet.add(FlagRuntimeType.valueOf("_" + rt.trim()));
                }
            }
            if (!(showAll || runtSet.contains(flag.getRuntime()))) {
                return false;
            }
        }
        {
            String type = UnixArgsutil.ARGS.kvalue("type");
            boolean showAll = ("ALL".equals(type) || (type == null));
            Set<FlagValueType> typeSet = new HashSet<FlagValueType>();
            if (!showAll) {
                String[] types = type.split(",");
                for (String ty : types) {
                    typeSet.add(FlagValueType.valueOf("_" + ty.trim()));
                }
            }
            if (!(showAll || typeSet.contains(flag.getType()))) {
                return false;
            }
        }

        return true;
    }

    private static void cutsFlags() {
        System.out.println(StringUtil.paddingSpaceRight("FlagName", 40) + " "
                           + StringUtil.paddingSpaceRight("Type", 20) + "Handler");
        System.out.println(StringUtil.repeat("-", 71));
        for (JFlagCommand flag : JFlagCommand.values()) {
            System.out.println(StringUtil.paddingSpaceRight(flag.name(), 40) + " "
                               + StringUtil.paddingSpaceRight(flag.getType().getName(), 20)
                               + flag.getHandler().getClass().getName());
        }
        System.exit(0);
    }

    private static void remoteFlags() {
        boolean writeable = UnixArgsutil.ARGS.flags().contains("writable");
        List<VMOption> optionList = new RemoteFlagTool(UnixArgsutil.ARGS.args()[0]).getHotSpotFlagMXBean().getVMOptionList();
        System.out.println(StringUtil.paddingSpaceRight("FlagName", 40) + " "
                           + StringUtil.paddingSpaceRight("Type", 20) + StringUtil.paddingSpaceRight("Writable", 10)
                           + "Value");
        System.out.println(StringUtil.repeat("-", 76));
        for (VMOption option : optionList) {
            if (filterName(option.getName())) {
                if (writeable && (!option.isWriteable())) {
                    continue;
                }
                System.out.println(StringUtil.paddingSpaceRight(option.getName(), 40) + " "
                                   + StringUtil.paddingSpaceRight(option.getOrigin().name(), 20)
                                   + StringUtil.paddingSpaceRight(Boolean.toString(option.isWriteable()), 10)
                                   + option.getValue());
            }
        }
        System.exit(0);
    }

    private static void flags(List<Flag> flagList) {
        System.out.println(StringUtil.paddingSpaceRight("FlagName", 40) + " "
                           + StringUtil.paddingSpaceRight("Type", 20) + "Runtime");
        System.out.println(StringUtil.repeat("-", 71));
        for (Flag flag : flagList) {
            if (filterFlag(flag)) {
                System.out.println(StringUtil.paddingSpaceRight(flag.getName(), 40) + " "
                                   + StringUtil.paddingSpaceRight(flag.getType().getName(), 20)
                                   + flag.getRuntime().getName());
            }
        }
        System.exit(0);
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar jflagall.jar [options] <pid>");
        System.out.println("    -show <flags>             show flags('ALL' show all)");
        System.out.println("    -filter <regex>           regex filter flag name");
        System.out.println("    -flag <+/-flag>           set flag");
        System.out.println("          <flag=value>        set flag to value");
        System.out.println("          <+/-~flag>          set flag by JMX");
        System.out.println("          <~flag=value>       set flag to value by JMX");
        System.out.println("    -runtime <option>         runtime(default all)");
        System.out.println("             options          " + Arrays.asList(FlagRuntimeType.values()));
        System.out.println("    -type <option>            type(default all)");
        System.out.println("          options             " + Arrays.asList(FlagValueType.values()));
        System.out.println("    --writable                show writable flag(s)");
        System.out.println("    --show-not-exists         show not exists flag(s)");
        System.out.println("    --show-cust-flags         show custom flags");
        System.out.println("    --show-remote-flags       show remote flags");
        System.out.println();
        System.out.println("Related read:");
        System.out.println("  http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html");
        System.out.println();
        System.out.println("Sample:");
        System.out.println("  java -jar jflagall.jar -show ALL --writable                              show all writable flags");
        System.out.println("  java -jar jflagall.jar --show-remote-flags --writable <PID>              show remote JVM all writeable flags");
        System.out.println("  java -jar jflagall.jar --show-cust-flags                                 show supported custome flags");
        System.out.println("  java -jar jflagall.jar -flag +PrintGCDetails <PID>                       open JVM's PrintGCDetails flag");
        System.out.println("  java -jar jflagall.jar -flag +~TraceClassLoading <PID>                   open JVM's TraceClassLoading flag using JMX");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }
}
