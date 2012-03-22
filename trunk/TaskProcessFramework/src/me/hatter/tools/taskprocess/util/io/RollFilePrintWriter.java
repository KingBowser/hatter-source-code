package me.hatter.tools.taskprocess.util.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import me.hatter.tools.taskprocess.util.env.Env;
import me.hatter.tools.taskprocess.util.misc.IOUtils;

public class RollFilePrintWriter implements Closeable {

    private String          basePath;
    private String          fileName;
    private FilePrintWriter writer;
    private long            rollCount;
    private boolean         append       = false;
    private AtomicInteger   rollIndex    = new AtomicInteger(0);
    private AtomicLong      currentCount = new AtomicLong(0);

    public RollFilePrintWriter(String fileName, long rollCount) {
        this(Env.USER_DIR, fileName, rollCount);
    }

    public RollFilePrintWriter(String fileName, long rollCount, boolean append) {
        this(Env.USER_DIR, fileName, rollCount, append);
    }

    public RollFilePrintWriter(String basePath, String fileName, long rollCount) {
        this(basePath, fileName, rollCount, true);
    }

    public RollFilePrintWriter(String basePath, String fileName, long rollCount, boolean append) {
        this.basePath = basePath;
        this.fileName = fileName;
        this.rollCount = rollCount;
        this.append = append;
        updateWriter();
    }

    synchronized public void println(String string) {
        currentCount.incrementAndGet();
        writer.println(string);
        if (currentCount.get() >= rollCount) {
            updateWriter();
        }
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.flush();
            IOUtils.closeQuietly(writer);
        }
    }

    void updateWriter() {
        try {
            if (writer == null) {
                writer = new FilePrintWriter(findNextFile(), this.append);
                return;
            }
            if (currentCount.get() >= rollCount) {
                writer.flush();
                IOUtils.closeQuietly(writer);
                currentCount.set(0);
                writer = new FilePrintWriter(findNextFile(), this.append);
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    File findNextFile() {
        while (true) {
            File f = new File(basePath, fileName + "." + (rollIndex.getAndIncrement()));
            if (!f.exists()) {
                System.out.println("[INFO] File is valid: " + f);
                return f;
            }
            System.out.println("[INFO] Skip exists file: " + f);
        }
    }
}
