package me.hatter.tools.cook.util;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.cook.exception.CookException;

public class CookLifeCycleManager {

    public static void fail(String errorMessage, String detailMessage) {
        List<LifeCycle> lifeCycleList = getLifeCycleList();
        for (LifeCycle lifeCycle : lifeCycleList) {
            lifeCycle.fail(errorMessage, detailMessage);
        }
    }

    public static void fail(Throwable t) {
        List<LifeCycle> lifeCycleList = getLifeCycleList();
        for (LifeCycle lifeCycle : lifeCycleList) {
            lifeCycle.fail(t);
        }
        throw new RuntimeException(t);
    }

    public static List<LifeCycle> getLifeCycleList() {
        List<LifeCycle> lifeCycleList = new ArrayList<LifeCycle>();
        // TODO ...
        return lifeCycleList;
    }
}
