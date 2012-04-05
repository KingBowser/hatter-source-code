package me.hatter.tools.cook.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import me.hatter.tools.cook.exception.GoalInitException;
import me.hatter.tools.cook.exception.GoalNotExistsException;
import me.hatter.tools.cook.goal.Goal;
import me.hatter.tools.cook.goal.annotation.Name;
import me.hatter.tools.cook.util.CookLifeCycleManager;

public class GoalLoader {

    private static Map<String, Goal> goalMap = new HashMap<String, Goal>();
    static {
        ServiceLoader<Goal> goalServiceLoader = ServiceLoader.load(Goal.class);
        Iterator<Goal> it = goalServiceLoader.iterator();
        while (it.hasNext()) {
            try {
                Goal goal = it.next();
                Name gname = goal.getClass().getAnnotation(Name.class);
                if (gname == null) {
                    throw new GoalInitException("Goal '" + goal.getClass().getName() + "' has no name assigned.");
                }
                goalMap.put(gname.value().toLowerCase(), goal);
            } catch (Throwable t) {
                CookLifeCycleManager.fail(t);
            }
        }
    }

    public static Goal getGoal(String name) {
        if ((name == null) || (name.trim().isEmpty())) {
            CookLifeCycleManager.fail("Goal is null or empty.", null);
        }
        Goal goal = goalMap.get(name.trim().toLowerCase());
        if (goal == null) {
            CookLifeCycleManager.fail(new GoalNotExistsException("Goal '" + name + "' not exists."));
        }
        return goal;
    }
}
