package me.hatter.tools.jtop.main.objects;

import java.util.HashMap;
import java.util.Map;

public class MainOutput {

    private int                     round;
    private int                     totalThreadCount;
    private long                    totalCpuTime;
    private long                    totalUserTime;
    private Map<Long, ThreadOutput> threadMap = new HashMap<Long, ThreadOutput>();

    public MainOutput(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public int getTotalThreadCount() {
        return totalThreadCount;
    }

    public void setTotalThreadCount(int totalThreadCount) {
        this.totalThreadCount = totalThreadCount;
    }

    public long getTotalCpuTime() {
        return totalCpuTime;
    }

    public void setTotalCpuTime(long totalCpuTime) {
        this.totalCpuTime = totalCpuTime;
    }

    public long getTotalUserTime() {
        return totalUserTime;
    }

    public void setTotalUserTime(long totalUserTime) {
        this.totalUserTime = totalUserTime;
    }

    public Map<Long, ThreadOutput> getThreadMap() {
        return threadMap;
    }

    public void setThreadMap(Map<Long, ThreadOutput> threadMap) {
        this.threadMap = threadMap;
    }
}
