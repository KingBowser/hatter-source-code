package me.hatter.tools.taskprocess.util.check;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import me.hatter.tools.taskprocess.util.env.Env;
import me.hatter.tools.taskprocess.util.misc.FileUtils;

public class ProcessStopCheck {

    private String        stopName;
    private File          flagFile;
    private long          checkMills;
    private AtomicBoolean stopFlag = new AtomicBoolean(false);

    public ProcessStopCheck() {
        this("stop.cmd", 3000);
    }

    public ProcessStopCheck(long checkMills) {
        this("stop.cmd", checkMills);
    }

    public ProcessStopCheck(String stopName, long checkMills) {
        System.out.println("[INFO] Stop flag: " + stopName);
        this.stopName = stopName;
        this.checkMills = checkMills;
        this.flagFile = new File(System.getProperty("user.dir"), stopName);

        if (flagFile.exists()) {
            System.out.println("[ERROR] Stop flag is ON!!!");
            System.exit(0);
        }

        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(ProcessStopCheck.this.checkMills);
                        if (flagFile.exists()) {
                            stopFlag.set(true);
                            System.out.println("[INFO] Stop flag changed to ON!");
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "StopFlagCheckThread").start();
    }

    public String readLastMessage() {
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

    public void writeLastMessage(String message) {
        File f = new File(Env.USER_DIR, stopName + ".message");
        try {
            FileUtils.writeStringToFile(f, message, Env.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkStopFlag() {
        return stopFlag.get();
    }
}
