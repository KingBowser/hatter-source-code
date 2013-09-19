package me.hatter.tools.commons.os.linux;

import java.io.File;

import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;

public class LoadAvgUtil {

    private static final LogTool logTool = LogTools.getLogTool(LoadAvgUtil.class);

    public static Loadavg getLoadavg() {
        File fProcLoadavg = new File(LinuxProc.LOADAVG);
        if (!fProcLoadavg.exists()) {
            return null;
        }
        String sProcLoadavg = FileUtil.readFileToString(fProcLoadavg);
        String[] sProcLoadavgs = sProcLoadavg.split("\\s+");
        if (sProcLoadavgs.length < 3) {
            logTool.error("Bad data in " + LinuxProc.LOADAVG + ": " + sProcLoadavg);
            return null;
        }
        double avg1 = Double.parseDouble(sProcLoadavgs[0]);
        double avg5 = Double.parseDouble(sProcLoadavgs[1]);
        double avg15 = Double.parseDouble(sProcLoadavgs[2]);
        return new Loadavg(avg1, avg5, avg15);
    }
}
