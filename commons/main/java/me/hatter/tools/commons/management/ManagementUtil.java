package me.hatter.tools.commons.management;

import java.lang.management.ManagementFactory;

public class ManagementUtil {

    public static String getCurrentVMPid() {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int p = nameOfRunningVM.indexOf('@');
        return nameOfRunningVM.substring(0, p);
    }
}
