package me.hatter.tools.jtop.rmi.interfaces;

import java.io.Serializable;
import java.lang.management.ThreadInfo;

public class JThreadInfo implements Serializable {

    private static final long   serialVersionUID = -8648817024047827694L;

    private long                cpuTime;
    private long                userTime;

    private String              threadName;
    private long                threadId;
    private long                blockedTime;
    private long                blockedCount;
    private long                waitedTime;
    private long                waitedCount;
    private String              lockName;
    private long                lockOwnerId;
    private String              lockOwnerName;
    private boolean             inNative;
    private boolean             suspended;
    private Thread.State        threadState;
    private StackTraceElement[] stackTrace;

    public JThreadInfo(ThreadInfo threadInfo, long cpuTime, long userTime) {
        this.threadName = threadInfo.getThreadName();
        this.threadId = threadInfo.getThreadId();
        this.blockedTime = threadInfo.getBlockedTime();
        this.blockedCount = threadInfo.getBlockedCount();
        this.waitedTime = threadInfo.getWaitedTime();
        this.waitedCount = threadInfo.getWaitedCount();
        this.lockName = threadInfo.getLockName();
        this.lockOwnerId = threadInfo.getLockOwnerId();
        this.lockOwnerName = threadInfo.getLockOwnerName();
        this.inNative = threadInfo.isInNative();
        this.suspended = threadInfo.isSuspended();
        this.threadState = threadInfo.getThreadState();
        this.stackTrace = threadInfo.getStackTrace();
        this.cpuTime = cpuTime;
        this.userTime = userTime;
    }

    public JThreadInfo(JThreadInfo jThreadInfo, long cpuTime, long userTime) {
        this.threadName = jThreadInfo.getThreadName();
        this.threadId = jThreadInfo.getThreadId();
        this.blockedTime = jThreadInfo.getBlockedTime();
        this.blockedCount = jThreadInfo.getBlockedCount();
        this.waitedTime = jThreadInfo.getWaitedTime();
        this.waitedCount = jThreadInfo.getWaitedCount();
        this.lockName = jThreadInfo.getLockName();
        this.lockOwnerId = jThreadInfo.getLockOwnerId();
        this.lockOwnerName = jThreadInfo.getLockOwnerName();
        this.inNative = jThreadInfo.isInNative();
        this.suspended = jThreadInfo.isSuspended();
        this.threadState = jThreadInfo.getThreadState();
        this.stackTrace = jThreadInfo.getStackTrace();
        this.cpuTime = cpuTime;
        this.userTime = userTime;
    }

    public long getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(long cpuTime) {
        this.cpuTime = cpuTime;
    }

    public long getUserTime() {
        return userTime;
    }

    public void setUserTime(long userTime) {
        this.userTime = userTime;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public long getBlockedTime() {
        return blockedTime;
    }

    public void setBlockedTime(long blockedTime) {
        this.blockedTime = blockedTime;
    }

    public long getBlockedCount() {
        return blockedCount;
    }

    public void setBlockedCount(long blockedCount) {
        this.blockedCount = blockedCount;
    }

    public long getWaitedTime() {
        return waitedTime;
    }

    public void setWaitedTime(long waitedTime) {
        this.waitedTime = waitedTime;
    }

    public long getWaitedCount() {
        return waitedCount;
    }

    public void setWaitedCount(long waitedCount) {
        this.waitedCount = waitedCount;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public long getLockOwnerId() {
        return lockOwnerId;
    }

    public void setLockOwnerId(long lockOwnerId) {
        this.lockOwnerId = lockOwnerId;
    }

    public String getLockOwnerName() {
        return lockOwnerName;
    }

    public void setLockOwnerName(String lockOwnerName) {
        this.lockOwnerName = lockOwnerName;
    }

    public boolean isInNative() {
        return inNative;
    }

    public void setInNative(boolean inNative) {
        this.inNative = inNative;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public Thread.State getThreadState() {
        return threadState;
    }

    public void setThreadState(Thread.State threadState) {
        this.threadState = threadState;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
    }
}
