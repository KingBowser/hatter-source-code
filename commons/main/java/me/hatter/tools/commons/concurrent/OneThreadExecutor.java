package me.hatter.tools.commons.concurrent;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class OneThreadExecutor extends AbstractExecutorService {

    volatile private boolean isShutDown = false;

    @Override
    public void shutdown() {
        isShutDown = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
        isShutDown = true;
        return null;
    }

    @Override
    public boolean isShutdown() {
        return isShutDown;
    }

    @Override
    public boolean isTerminated() {
        return isShutDown;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return true;
    }

    @Override
    public void execute(Runnable command) {
        if (isShutDown) {
            throw new IllegalStateException("Executor is shutdown.");
        }
        command.run();
    }
}
