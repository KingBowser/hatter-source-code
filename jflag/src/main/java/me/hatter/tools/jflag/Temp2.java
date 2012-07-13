package me.hatter.tools.jflag;

import java.util.List;

import me.hatter.tools.commons.jmx.RemoteManagementFactory;
import me.hatter.tools.commons.jmx.RemoteManagementTool;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;

public class Temp2 {

    public static void main(String[] args) {
        RemoteManagementTool tool = new RemoteManagementTool("1347");
        RemoteManagementFactory factory = tool.getManagementFactory();
        HotSpotDiagnosticMXBean diagnostic = factory.getHotSpotDiagnosticMXBean();
        List<VMOption> options = diagnostic.getDiagnosticOptions();
        for (VMOption o : options) {
            System.out.println(o);
        }
        System.out.println("===============================");
        // diagnostic.setVMOption("UseNUMA", "true");
        System.out.println(diagnostic.getVMOption("UseNUMA"));

        diagnostic.setVMOption("PrintGCDetails", "false");
    }
}
