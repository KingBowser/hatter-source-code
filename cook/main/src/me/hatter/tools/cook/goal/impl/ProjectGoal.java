package me.hatter.tools.cook.goal.impl;

import java.io.File;

import me.hatter.tools.cook.goal.AbstractGoal;
import me.hatter.tools.cook.goal.CookContext;
import me.hatter.tools.cook.goal.Goal;
import me.hatter.tools.cook.goal.annotation.Name;

@Name("project")
public class ProjectGoal extends AbstractGoal {

    @TaskName("create")
    public void runCreateTask(CookContext context) {
        String[] dirs = new String[] { "main/java", "main/conf", "test/java", "test/conf" };
        for (String dir : dirs) {
            File f = new File(Goal.USER_DIR, dir);
            if (f.exists()) {
                System.out.println("[INFO] Dir areadly exists: " + f);
            } else {
                System.out.println("[INFO] Create dir: " + f);
                f.mkdirs();
            }
        }
    }
}
