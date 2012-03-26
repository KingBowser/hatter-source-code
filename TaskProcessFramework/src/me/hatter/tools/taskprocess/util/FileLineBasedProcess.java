package me.hatter.tools.taskprocess.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import me.hatter.tools.taskprocess.util.check.ProcessStopFlag;
import me.hatter.tools.taskprocess.util.concurrent.DayNightProcessExecuteService;
import me.hatter.tools.taskprocess.util.env.Env;
import me.hatter.tools.taskprocess.util.io.FileBufferedReader;
import me.hatter.tools.taskprocess.util.io.RollFilePrintWriter;
import me.hatter.tools.taskprocess.util.misc.ExceptionUtils;
import me.hatter.tools.taskprocess.util.misc.StringUtils;

public abstract class FileLineBasedProcess {

    protected static final boolean      isDryRun         = Env.getBoolPropertyOrDie("dryrun");
    protected static final String       dataFile         = Env.getPropertyOrDie("datafile");
    protected static BufferedReader     dataReader       = null;
    static {
        try {
            File dataf = Env.newUserDirFile(dataFile);
            System.out.println("[INFO] data file:  " + dataf);
            dataReader = new FileBufferedReader(dataf);
        } catch (IOException e) {
            System.out.println("[ERROR] Open data file failed: " + ExceptionUtils.getStackTrace(e));
            System.exit(0);
        }
    }
    protected final RollFilePrintWriter dataLog          = new RollFilePrintWriter(dataFile + ".log",
                                                                                   getDataFileRollCount(), true);
    protected final RollFilePrintWriter failLog          = new RollFilePrintWriter(dataFile + ".fail.log",
                                                                                   getFailFileRollCount(), true);
    protected final AtomicInteger       skipToLine       = new AtomicInteger(Env.getIntProperty("skiptoline", 0));
    protected final int                 threadCountDay   = Env.getIntProperty("threadcountday", 10);
    protected final int                 threadCountNight = Env.getIntProperty("threadcountnight", 30);
    protected final ProcessStopFlag     processStopFlag  = newProcessStopCheck();
    protected final AtomicInteger       totalCount       = new AtomicInteger(0);
    protected final AtomicInteger       thisCount        = new AtomicInteger(0);

    // do actual work
    abstract protected void doProcess(String line, boolean isDryRun) throws Exception;

    protected void mainProcss() {
        try {
            dataLog.println("[LOGGING] >>>>>>>>>> Start at: " + new Date() + " >>>>>>>>>>");
            failLog.println("[LOGGING] >>>>>>>>>> Start at: " + new Date() + " >>>>>>>>>>");

            dealSkipToLine();
            skipingToLine();

            final DayNightProcessExecuteService processExecuteService = new DayNightProcessExecuteService(
                                                                                                          threadCountDay,
                                                                                                          threadCountNight);
            System.out.println("[INFO] Start processing: " + new Date());
            System.out.println("[INFO] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            final long start = System.currentTimeMillis();
            for (String line; ((line = dataReader.readLine()) != null);) {
                if (processStopFlag.checkStopFlag()) {
                    processStopFlag.writeLastMessage("" + totalCount.get());
                    break; // END
                }

                totalCount.incrementAndGet();
                if (StringUtils.isEmpty(line)) {
                    continue; // skip empty line(s)
                }

                processStopFlag.sendLastMessage("" + totalCount.get());
                final String theLine = line;
                Callable<Void> task = new Callable<Void>() {

                    public Void call() throws Exception {
                        try {
                            printDataLog(processExecuteService, start, thisCount.incrementAndGet());
                            doProcess(theLine, isDryRun);
                        } catch (Exception e) {
                            failLog.println(theLine);
                            throw e;
                        }
                        return null;
                    }
                };
                processExecuteService.submit(getProtectedCallable(task));
            }

            System.out.println("[INFO] Waiting all processing task(s) to finish, ramain task count: "
                               + processExecuteService.getRunningCount() + "/" + processExecuteService.getTotalCount());
            processExecuteService.waitUntilFinish();
            System.out.println("[INFO] Closing file handler(s).");
            dataLog.println("[LOGGING] <<<<<<<<<< End at: " + new Date() + " <<<<<<<<<<");
            failLog.println("[LOGGING] <<<<<<<<<< End at: " + new Date() + " <<<<<<<<<<");
            failLog.close();
            dataLog.close();
            dataReader.close();

            System.out.println("[INFO] Processed current count: " + this.thisCount.get() + ", all count: "
                               + this.totalCount.get());
            System.out.println("[INFO] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("[INFO] Finish!!!!!!! " + new Date());
            processExecuteService.shutDown();
        } catch (Throwable t) {
            System.out.println("[ERROR] UNKNOW ERROR!!!!!!!  " + ExceptionUtils.getStackTrace(t));
        }
        System.exit(0);
    }

    protected Callable<Void> getProtectedCallable(Callable<Void> task) {
        return new ProcessDelayProtection<Void>(task);
    }

    protected void printDataLog(final DayNightProcessExecuteService processExecuteService, final long start, long ccount) {
        dataLog.println("[INFO] CT:" + ccount + ", CO:" + (System.currentTimeMillis() - start) + " ms" + ", CC:"
                        + processExecuteService.getRunningCount());
        printSummaryDataLog(processExecuteService, start, ccount);
    }

    private void printSummaryDataLog(final DayNightProcessExecuteService processExecuteService, final long start,
                                     long ccount) {
        if ((ccount % getSummaryInterval()) == 0) {
            long current = System.currentTimeMillis();
            long cost = current - start;
            System.out.println("[INFO] Total count: " + totalCount.get() + ", Count: " + ccount + ", Time: " + cost
                               + " ms, Average: " + (cost / ccount) + " ms/product");
        }
    }

    // skip to the assigned item
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

    // run next after last processed item
    protected void dealSkipToLine() {
        if (skipToLine.get() == 0) {
            String message = processStopFlag.readLastMessage();
            if (StringUtils.isNotEmpty(message)) {
                int lastLine = Integer.parseInt(message);
                skipToLine.set(lastLine);
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

    protected ProcessStopFlag newProcessStopCheck() {
        return new ProcessStopFlag();
    }
}
