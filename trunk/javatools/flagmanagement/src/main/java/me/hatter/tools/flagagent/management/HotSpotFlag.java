package me.hatter.tools.flagagent.management;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;
import javax.management.StandardMBean;

import sun.management.FlagAgent;

import com.sun.management.VMOption;

public class HotSpotFlag extends StandardMBean implements HotSpotFlagMXBean {

    private static HotSpotFlag HOT_SPOT_FLAG_MXBEAN = null;

    public HotSpotFlag() {
        super(HotSpotFlagMXBean.class, true);
    }

    synchronized public static void registerMXBean() {
        if (HOT_SPOT_FLAG_MXBEAN != null) {
            return;
        }
        try {
            HOT_SPOT_FLAG_MXBEAN = new HotSpotFlag();
            ManagementFactory.getPlatformMBeanServer().registerMBean(HOT_SPOT_FLAG_MXBEAN,
                                                                     new ObjectName(HOTSPOT_FLAG_MXBEAN_NAME));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<VMOption> getVMOptionList() {
        List<VMOption> optionList = new ArrayList<VMOption>();
        List<FlagAgent> flagList = FlagAgent.getAllFlags();
        for (FlagAgent flag : flagList) {
            optionList.add(flag.getVMOption());
        }
        return optionList;
    }

    public VMOption getVMOption(String name) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }

        FlagAgent f = FlagAgent.getFlag(name);
        if (f == null) {
            throw new IllegalArgumentException("VM option \"" + name + "\" does not exist");
        }
        return f.getVMOption();
    }

    public void setVMOption(String name, String value) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }

        FlagAgent flag = FlagAgent.getFlag(name);
        if (flag == null) {
            throw new IllegalArgumentException("VM option \"" + name + "\" does not exist");
        }
        if (!flag.isWriteable()) {
            throw new IllegalArgumentException("VM Option \"" + name + "\" is not writeable");
        }

        // Check the type of the value
        Object v = flag.getValue();
        if (v instanceof Long) {
            try {
                long l = Long.parseLong(value);
                FlagAgent.setLongValue(name, l);
            } catch (NumberFormatException e) {
                IllegalArgumentException iae = new IllegalArgumentException("Invalid value:" + " VM Option \"" + name
                                                                            + "\"" + " expects numeric value");
                iae.initCause(e);
                throw iae;
            }
        } else if (v instanceof Boolean) {
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                throw new IllegalArgumentException("Invalid value:" + " VM Option \"" + name + "\""
                                                   + " expects \"true\" or \"false\".");
            }
            FlagAgent.setBooleanValue(name, Boolean.parseBoolean(value));
        } else if (v instanceof String) {
            FlagAgent.setStringValue(name, value);
        } else {
            throw new IllegalArgumentException("VM Option \"" + name + "\" is of an unsupported type: "
                                               + v.getClass().getName());
        }
    }

}
