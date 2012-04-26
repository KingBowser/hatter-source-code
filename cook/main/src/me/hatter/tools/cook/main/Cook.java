package me.hatter.tools.cook.main;

import java.util.List;

import me.hatter.tools.cook.goal.CookContext;
import me.hatter.tools.cook.goal.Goal;
import me.hatter.tools.cook.goal.GoalTask;
import me.hatter.tools.cook.util.CookArgs;
import me.hatter.tools.cook.util.CookArgsUtil;
import me.hatter.tools.cook.util.Env;
import me.hatter.tools.cook.util.GoalTaskUtil;

public class Cook {

    public static void main(String[] args) {
        System.out.println("[INFO] Cook started");
        boolean hasError = false;
        try {
            CookContext context = buildContext(args);

            // run goals
            for (GoalTask goalTask : context.getGoalTaskList()) {
                Goal goal = GoalLoader.getGoal(goalTask.getGoal());
                goal.run(context, goalTask.getTask());
            }
        } catch (Throwable t) {
            t.printStackTrace();
            hasError = true;
        }
        System.out.println("[INFO] FINISH " + (hasError ? "FAILED" : "SUCCESS"));
    }

    public static CookContext buildContext(String[] args) {
        args = Env.parseArgs(args); // parse arguments
        CookArgs cookArgs = new CookArgs();
        args = CookArgsUtil.parseCookArgs(cookArgs, args);
        List<GoalTask> goalTaskList = GoalTaskUtil.parseGoalTaskList(args);

        CookContext context = new CookContext();
        context.setCookArgs(cookArgs);
        context.setGoalTaskList(goalTaskList);

        return context;
    }
}
