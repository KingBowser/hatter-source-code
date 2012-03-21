package me.hatter.tools.taskprocess.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import me.hatter.tools.taskprocess.util.check.ProcessStopCheck;
import me.hatter.tools.taskprocess.util.concurrent.ProcessExecuteService;
import me.hatter.tools.taskprocess.util.io.FileBufferedReader;
import me.hatter.tools.taskprocess.util.io.RollFilePrintWriter;
import me.hatter.tools.taskprocess.util.misc.StringUtils;

public abstract class FileLineBasedProcess {

    protected static final String       USER_DIR         = System.getProperty("user.dir");
    protected static final boolean      isDryRun;
    static {
        String dryRun = System.getProperty("dryrun");
        if (dryRun == null) {
            System.out.println("[ERROR] dryrun is null.");
            System.exit(0);
        }
        isDryRun = "Y".equalsIgnoreCase(dryRun);
        System.out.println("[INFO] is dry run mode:  " + isDryRun);
    }
    protected static final String       dataFile         = System.getProperty("datafile");
    static {
        if (dataFile == null) {
            System.out.println("[ERROR] datafile is null.");
            System.exit(0);
        }
    }
    protected static BufferedReader     dataReader       = null;
    static {
        try {
            File dataf = new File(USER_DIR, dataFile);
            System.out.println("[INFO] data file:  " + dataf);
            dataReader = new FileBufferedReader(dataf);
        } catch (IOException e) {
            System.out.println("[ERROR] Open data file failed.");
            e.printStackTrace();
            System.exit(0);
        }
    }
    protected final RollFilePrintWriter dataLog          = new RollFilePrintWriter(USER_DIR, dataFile + ".log",
                                                                                   getDataFileRollCount(), true);
    protected final RollFilePrintWriter failLog          = new RollFilePrintWriter(USER_DIR, dataFile + ".fail.log",
                                                                                   getFailFileRollCount(), true);
    protected final AtomicInteger       skipToLine       = new AtomicInteger(
                                                                             Integer.parseInt(System.getProperty("skiptoline",
                                                                                                                 "0")));
    protected final int                 threadCountNight = Integer.parseInt(System.getProperty("threadcountnight", "30"));
    protected final int                 threadCountDay   = Integer.parseInt(System.getProperty("threadcountday", "5"));
    protected final ProcessStopCheck    processStopCheck = newProcessStopCheck();
    protected final AtomicInteger       totalCount       = new AtomicInteger(0);
    protected final AtomicInteger       thisCount        = new AtomicInteger(0);

    abstract protected void doProcess(String line) throws Exception;

    protected void mainProcss() {
        try {
            dataLog.println("[LOGGING] >>>>>>>>>> Start at: " + new Date() + " >>>>>>>>>>");
            failLog.println("[LOGGING] >>>>>>>>>> Start at: " + new Date() + " >>>>>>>>>>");

            dealSkipToLine();
            skipingToLine();

            final ProcessExecuteService processExecuteService = new ProcessExecuteService(threadCountDay,
                                                                                          threadCountNight);
            System.out.println("[INFO] Start processing: " + new Date());
            System.out.println("[INFO] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            final long start = System.currentTimeMillis();
            for (String line; ((line = dataReader.readLine()) != null);) {
                if (processStopCheck.checkStopFlag()) {
                    processStopCheck.writeLastMessage("" + totalCount.get());
                    break; // END
                }

                totalCount.incrementAndGet();
                if (StringUtils.isEmpty(line)) {
                    continue; // skip empty line(s)
                }

                final String theLine = line;
                processExecuteService.submit(new Callable<Void>() {

                    public Void call() throws Exception {
                        try {
                            long ccount = thisCount.incrementAndGet();
                            dataLog.println("[INFO] CT:" + ccount + ", CO:" + (System.currentTimeMillis() - start)
                                            + " ms" + ", CC:" + processExecuteService.getRunningCount());
                            if ((ccount % getSummaryInterval()) == 0) {
                                System.out.println("[INFO] Total count: " + totalCount.get() + ", Count: " + ccount
                                                   + ", Time: " + (System.currentTimeMillis() - start)
                                                   + " ms, Average: " + ((System.currentTimeMillis() - start) / ccount)
                                                   + " ms/product");
                            }
                            doProcess(theLine);
                        } catch (Exception e) {
                            failLog.println(theLine);
                            throw e;
                        }
                        return null;
                    }
                });
            }

            System.out.println("[INFO] Waiting all process task to finish, ramain task count: "
                               + processExecuteService.getRunningCount() + "/" + processExecuteService.getTotalCount());
            processExecuteService.waitUntilFinish();
            System.out.println("[INFO] Closing file handler(s).");
            dataLog.println("[LOGGING] <<<<<<<<<< End at: " + new Date() + " <<<<<<<<<<");
            failLog.println("[LOGGING] <<<<<<<<<< End at: " + new Date() + " <<<<<<<<<<");
            failLog.close();
            dataLog.close();
            dataReader.close();

            System.out.println("[INFO] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("[INFO] Finish!!!!!!! " + new Date());
            processExecuteService.shutDown();
        } catch (Throwable t) {
            System.out.println("[ERROR] UNKNOW ERROR!!!!!!!  " + t.getMessage());
            t.printStackTrace();
        }
        System.exit(0);
    }

    protected void skipingToLine() throws IOException {
        if (skipToLine.get() > 0) {
            System.out.println("[INFO] skiping line(s): " + skipToLine);
            String lastLine = null;
            for (int i = 0; i < skipToLine.get(); i++) {
                totalCount.incrementAndGet();
                if ((i > 0) && ((i % 1000) == 0)) {
                    System.out.println("[INFO] skip to line: " + i);
                }
                lastLine = dataReader.readLine();
            }
            System.out.println("[INFO] skiping line(s) finish, last line: " + lastLine);
        }
    }

    protected void dealSkipToLine() {
        if (skipToLine.get() == 0) {
            String message = processStopCheck.readLastMessage();
            if (StringUtils.isNotEmpty(message)) {
                int lastLine = Integer.parseInt(message);
                if (lastLine > 50) {
                    skipToLine.set(lastLine - 50);
                } else {
                    skipToLine.set(lastLine);
                }
            }
        }
        System.out.println("[INFO] skip to line: " + skipToLine.get());
    }

    // ---- config ----
    protected long getSummaryInterval() {
        return 1000;
    }

    protected long getDataFileRollCount() {
        return 10000000;
    }

    protected long getFailFileRollCount() {
        return 10000000;
    }

    protected ProcessStopCheck newProcessStopCheck() {
        return new ProcessStopCheck();
    }
}
