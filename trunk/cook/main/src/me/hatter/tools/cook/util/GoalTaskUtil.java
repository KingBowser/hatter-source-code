package me.hatter.tools.cook.util;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.cook.goal.CookContext;
import me.hatter.tools.cook.goal.GoalTask;

public class GoalTaskUtil {

    public static List<GoalTask> parseGoalTaskList(String[] args) {
        List<GoalTask> goalTaskList = new ArrayList<GoalTask>();
        if (args != null) {
            for (String arg : args) {
                int indexOfC = arg.indexOf(':');
                String goal;
                String task = null;
                if (indexOfC < 0) {
                    goal = arg;
                } else if (indexOfC == 0) {
                    goal = CookContext.DEFAULT_GOAL;
                    task = arg;
                } else {
                    goal = arg.substring(0, indexOfC);
                    task = arg.substring(indexOfC + 1);
                }
                goalTaskList.add(new GoalTask(goal, task));
            }
        }
        return goalTaskList;
    }
}
