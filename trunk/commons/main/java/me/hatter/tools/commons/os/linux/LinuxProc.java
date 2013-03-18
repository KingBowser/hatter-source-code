package me.hatter.tools.commons.os.linux;

// http://www.kernel.org/doc/man-pages/online/pages/man5/proc.5.html
public class LinuxProc {

    public static final String UPTIME      = "/proc/uptime";
    public static final String LOADAVG     = "/proc/loadavg";
    public static final String STAT        = "/proc/stat";
    public static final String VMSTAT      = "/proc/vmstat";
    public static final String MEMINFO     = "/proc/meminfo";
    public static final String SLABINFO    = "/proc/slabinfo";
    public static final String KSYMS       = "/proc/ksyms";
    public static final String DISKSTATS   = "/proc/diskstats";
    public static final String TTY_DRIVERS = "/proc/tty/drivers";

    public static final String NNN_CWD     = "/proc/$NNN$/cwd";
    public static final String NNN_STAT    = "/proc/$NNN$/stat";
    public static final String NNN_PSINFO  = "/proc/$NNN$/psinfo";
    public static final String NNN_STATUS  = "/proc/$NNN$/status";
    public static final String NNN_ENVIRON = "/proc/$NNN$/environ";
    public static final String NNN_CMDLINE = "/proc/$NNN$/cmdline";
    public static final String NNN_STATM   = "/proc/$NNN$/statm";
    public static final String NNN_WCHAN   = "/proc/$NNN$/wchan";
    public static final String NNN_MAPS    = "/proc/$NNN$/maps";
}
