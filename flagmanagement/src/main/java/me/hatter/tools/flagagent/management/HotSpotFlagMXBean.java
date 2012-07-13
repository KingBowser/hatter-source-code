package me.hatter.tools.flagagent.management;

import java.util.List;

import com.sun.management.VMOption;

public interface HotSpotFlagMXBean {

    public static final String HOTSPOT_FLAG_MXBEAN_NAME = "me.hatter.management:type=HotSpotFlag";

    List<VMOption> getVMOptionList();

    VMOption getVMOption(String name);

    void setVMOption(String name, String value);
}
