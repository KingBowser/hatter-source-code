package me.hatter.tools.commons.jvm;

import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.hatter.tools.commons.management.ManagementUtil;
import me.hatter.tools.commons.string.StringUtil;
import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.Monitor;
import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

@SuppressWarnings("restriction")
public class HotSpotProcessUtil {

    public static interface VMVisitor {

        void visit(int pid, MonitoredVm vm);
    }

    public static void printVMs(PrintStream out, boolean ignoreCurrentVM) {
        List<HotSpotProcessVM> vms = listVMs();
        out.println(StringUtil.paddingSpaceRight("jvmid", 10) + StringUtil.paddingSpaceRight("attachable", 13)
                    + "classname");
        String currentPid = ManagementUtil.getCurrentVMPid();
        int printedVMCount = 0;
        for (HotSpotProcessVM vm : vms) {
            if (ignoreCurrentVM && currentPid.equals(String.valueOf(vm.getPid()))) {
                continue;
            }
            printedVMCount++;
            out.println(StringUtil.paddingSpaceRight(String.valueOf(vm.getPid()), 10)
                        + StringUtil.paddingSpaceRight(String.valueOf(vm.isAttachAble()), 13) + vm.getFullClassName());
        }
        if (printedVMCount == 0) {
            out.println("== no java vm instance found ==");
        }
    }

    public static List<HotSpotProcessVM> listVMs() {
        final List<HotSpotProcessVM> vmList = new ArrayList<HotSpotProcessVM>();
        iteratorVMs(new VMVisitor() {

            @Override
            public void visit(int pid, MonitoredVm vm) {
                HotSpotProcessVM hvm = new HotSpotProcessVM();
                hvm.setPid(pid);
                try {
                    hvm.setFullClassName(MonitoredVmUtil.mainClass(vm, true));
                    hvm.setAttachAble(MonitoredVmUtil.isAttachable(vm));
                } catch (MonitorException e) {
                    // IGNORE
                }
                vmList.add(hvm);
            }
        });
        return vmList;
    }

//    @SuppressWarnings("unchecked")
    public static void iteratorVMs(VMVisitor visitor) {
        try {
            MonitoredHost host = getMonitoredHost();
            Set<Integer> jvms = host.activeVms();
            for (Integer jvmid : jvms) {
                MonitoredVm vm = getMonitoredVm(host, jvmid.intValue());
                try {
                    visitor.visit(jvmid.intValue(), vm);
                } finally {
                    host.detach(vm);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @SuppressWarnings("unchecked")
    public static Map<String, String> listVMMonitor(int pid) throws URISyntaxException, MonitorException {
        MonitoredHost host = getMonitoredHost();
        MonitoredVm vm = getMonitoredVm(host, pid);
        Map<String, String> result = new LinkedHashMap<String, String>();
        List<Monitor> ms = vm.findByPattern(".*");
        for (Monitor m : ms) {
            result.put(m.getName(), String.valueOf(m.getValue()));
        }
        return result;
    }

    public static MonitoredHost getMonitoredHost() throws URISyntaxException, MonitorException {
        HostIdentifier hostId = new HostIdentifier((String) null);
        MonitoredHost host = MonitoredHost.getMonitoredHost(hostId);
        return host;
    }

    public static MonitoredVm getMonitoredVm(MonitoredHost host, int pid) throws URISyntaxException, MonitorException {
        String vmidString = "//" + pid + "?mode=r";
        VmIdentifier id = new VmIdentifier(vmidString);
        MonitoredVm vm = host.getMonitoredVm(id, 0);
        return vm;
    }
}
