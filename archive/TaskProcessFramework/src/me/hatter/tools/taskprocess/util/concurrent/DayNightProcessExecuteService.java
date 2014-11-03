package me.hatter.tools.taskprocess.util.concurrent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DayNightProcessExecuteService extends AbstractProcessExecuteService {

    private int             dayTimeThreadCount;
    private int             nightThreadCount;
    private ExecutorService dayTimeExecutor;
    private ExecutorService nightExecutor;

    public DayNightProcessExecuteService(int dayTimeThreadCount, int nightThreadCount) {
        this((Math.max(dayTimeThreadCount, nightThreadCount) + 10), dayTimeThreadCount, nightThreadCount);
    }

    public DayNightProcessExecuteService(int maxQueueCount, int dayTimeThreadCount, int nightThreadCount) {
        super(maxQueueCount);
        this.dayTimeThreadCount = dayTimeThreadCount;
        this.nightThreadCount = nightThreadCount;
        // create excutors and semaphore
        dayTimeExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.dayTimeThreadCount);
        nightExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.nightThreadCount);
        System.out.println("[INFO] Process executor service; maxQueueCount: " + maxQueueCount
                           + ", dayTimeThreadCount: " + dayTimeThreadCount + ", nightThreadCount: " + nightThreadCount);
    }

    protected ExecutorService getCurrentExecutor() {
        return (isDayTime()) ? dayTimeExecutor : nightExecutor;
    }

    protected List<ExecutorService> getAllExecutors() {
        return Arrays.asList(dayTimeExecutor, nightExecutor);
    }

    protected boolean isDayTime() {
        int hzHour = getHzHour();
        return ((hzHour > 8) && (hzHour < 20));
    }

    protected Integer getHzHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return new Integer(sdf.format(new Date()));
    }
}
