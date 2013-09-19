package me.hatter.tools.commons.os.linux;

import java.io.File;

import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;

public class UptimeUtil {

    private static final LogTool logTool = LogTools.getLogTool(UptimeUtil.class);

    public static Uptime getUptime() {
        File fProcUptime = new File(LinuxProc.UPTIME);
        if (!fProcUptime.exists()) {
            return null;
        }
        String sUptime = FileUtil.readFileToString(fProcUptime);
        String[] sUptimes = sUptime.split("\\s+");
        if (sUptimes.length < 2) {
            logTool.error("Bad data in " + LinuxProc.UPTIME + ": " + sUptime);
            return null;
        }
        double up = Double.parseDouble(sUptimes[0]);
        double idle = Double.parseDouble(sUptimes[1]);
        return new Uptime(up, idle);
    }
}
