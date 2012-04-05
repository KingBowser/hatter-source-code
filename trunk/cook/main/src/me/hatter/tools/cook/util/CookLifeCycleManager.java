package me.hatter.tools.cook.util;

import java.util.ArrayList;
import java.util.List;

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
    }

    public static List<LifeCycle> getLifeCycleList() {
        List<LifeCycle> lifeCycleList = new ArrayList<LifeCycle>();
        // TODO ...
        return lifeCycleList;
    }
}
