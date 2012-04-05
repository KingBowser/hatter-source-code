package me.hatter.tools.cook.goal;

import java.util.List;

import me.hatter.tools.cook.util.CookArgs;

public class CookContext {

    public static final String DEFAULT_GOAL = "default";

    private CookArgs           cookArgs;
    private List<GoalTask>     goalTaskList;

    public CookArgs getCookArgs() {
        return cookArgs;
    }

    public void setCookArgs(CookArgs cookArgs) {
        this.cookArgs = cookArgs;
    }

    public List<GoalTask> getGoalTaskList() {
        return goalTaskList;
    }

    public void setGoalTaskList(List<GoalTask> goalTaskList) {
        this.goalTaskList = goalTaskList;
    }
}
