package me.hatter.tools.taskprocess.util.check;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import me.hatter.tools.taskprocess.util.env.Env;
import me.hatter.tools.taskprocess.util.misc.ExceptionUtils;
import me.hatter.tools.taskprocess.util.misc.FileUtils;

public class ProcessStopFlag {

    private static final long       DEF_CHECK_MILLS = Env.getLongProperty("defcheckmills", 3000);
    private static final long       DEF_WRITE_MILLS = Env.getLongProperty("defsavemills", 10000);
    private static final String     DEF_STOP_FLAG   = Env.getProperty("defstopflag", "cmd.stop");
    private String                  stopName;
    private File                    flagFile;
    private long                    checkMills;
    private long                    writeMills;
    private AtomicReference<String> beforeMessage   = new AtomicReference<String>(null);
    private AtomicReference<String> lastMessage     = new AtomicReference<String>(null);
    private AtomicBoolean           stopFlag        = new AtomicBoolean(false);

    public ProcessStopFlag() {
        this(DEF_CHECK_MILLS);
    }

    public ProcessStopFlag(long checkMills) {
        this(DEF_STOP_FLAG, checkMills);
    }

    public ProcessStopFlag(String stopName, long checkMills) {
        System.out.println("[INFO] Stop flag: " + stopName);
        this.stopName = stopName;
        this.checkMills = checkMills;
        this.writeMills = DEF_WRITE_MILLS;
        this.flagFile = new File(Env.USER_DIR, stopName);

        if (flagFile.exists()) {
            System.out.println("[ERROR] Stop flag is ON!!!");
            System.exit(0);
        }

        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(ProcessStopFlag.this.checkMills);
                        if (flagFile.exists()) {
                            synchronized (ProcessStopFlag.this) {
                                stopFlag.set(true);
                                lastMessage.set(null);
                            }
                            System.out.println("[INFO] Stop flag changed to ON!");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("[WARN] Error in check stop flag: " + ExceptionUtils.getStackTrace(e));
                    }
                }
            }
        }, "StopFlagCheckThread").start();

        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(ProcessStopFlag.this.writeMills);
                        synchronized (ProcessStopFlag.this) {
                            if (stopFlag.get()) {
                                System.out.println("[INFO] Stop flag is ON, do not write message any more.");
                                break;
                            }
                            String beforeMsg = beforeMessage.get();
                            String lastMsg = lastMessage.get();
                            if (lastMsg != null) {
                                if ((beforeMsg == null) || (!lastMsg.equals(beforeMsg))) {
                                    // Write last message
                                    System.out.println("[INFO] Synchronized last message to file; before: " + beforeMsg
                                                       + ", last: " + lastMsg);
                                    writeLastMessage(lastMsg);
                                    beforeMessage.set(lastMsg);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("[WARN] Error in write message: " + ExceptionUtils.getStackTrace(e));
                    }
                }
            }
        }, "StopFlagWriteMessageThread").start();
    }

    synchronized public String readLastMessage() {
        File f = new File(Env.USER_DIR, stopName + ".message");
        if (!f.exists()) {
            return "";
        }
        try {
            return FileUtils.readFileToString(f, Env.UTF_8).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized public void writeLastMessage(String message) {
        File f = new File(Env.USER_DIR, stopName + ".message");
        try {
            FileUtils.writeStringToFile(f, message, Env.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendLastMessage(String message) {
        lastMessage.set(message);
    }

    public boolean checkStopFlag() {
        return stopFlag.get();
    }
}
