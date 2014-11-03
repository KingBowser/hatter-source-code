package me.hatter.tools.jflag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.hatter.tools.commons.jmx.RemoteManagementTool;
import me.hatter.tools.commons.jvm.HotSpotAttachTool;
import me.hatter.tools.flagagent.management.HotSpotFlagMXBean;

public class RemoteFlagTool {

    private String                   pid;

    private static HotSpotFlagMXBean hotSpotFlagMXBean = null;

    public RemoteFlagTool(String pid) {
        this.pid = pid;
    }

    synchronized public HotSpotFlagMXBean getHotSpotFlagMXBean() {
        if (hotSpotFlagMXBean == null) {
            HotSpotAttachTool attach = new HotSpotAttachTool(pid);
            attach.attach();
            try {
                if (attach.getVM().getSystemProperties().get("me.hatter.management.init.HotSpotFlag") == null) {
                    attach.getVM().loadAgent(getTempFlagAgent().getAbsolutePath());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                attach.detach();
            }
            RemoteManagementTool tool = new RemoteManagementTool(pid);
            hotSpotFlagMXBean = tool.getManagementFactory().newPlatformMXBeanProxy(HotSpotFlagMXBean.HOTSPOT_FLAG_MXBEAN_NAME,
                                                                                   HotSpotFlagMXBean.class);
        }
        return hotSpotFlagMXBean;
    }

    private static File tempFlagAgent;

    synchronized private static File getTempFlagAgent() {
        if (tempFlagAgent == null) {
            try {
                tempFlagAgent = File.createTempFile("flagagent", ".jar");
                tempFlagAgent.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(tempFlagAgent);
                copy(RemoteFlagTool.class.getResourceAsStream("/flagagent.jar"), fos);
                fos.close();
                System.out.println("[INFO] Load agent: " + tempFlagAgent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return tempFlagAgent;
    }

    private static long copy(InputStream is, OutputStream os) throws IOException {
        long total = 0;
        for (int b = 0; ((b = is.read()) != -1);) {
            os.write(b);
            total++;
        }
        return total;
    }
}
