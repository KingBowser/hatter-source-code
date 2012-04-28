package me.hatter.tools.cook.goal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hatter.tools.cook.util.ConsoleUtil;
import me.hatter.tools.cook.util.CookArgs;

public class CookContext {

    public static final String  DEFAULT_GOAL = "default";

    private CookArgs            cookArgs;
    private List<GoalTask>      goalTaskList;
    private Map<String, String> inputMap     = new HashMap<String, String>();

    public String getInput(String key, String prompt) {
        String input = inputMap.get(key);
        if (input != null) {
            return input;
        }
        input = ConsoleUtil.readLie(prompt);
        inputMap.put(key, input);
        return input;
    }

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
