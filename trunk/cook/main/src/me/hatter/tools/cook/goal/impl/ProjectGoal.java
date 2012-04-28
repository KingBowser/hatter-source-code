package me.hatter.tools.cook.goal.impl;

import java.io.File;

import me.hatter.tools.cook.goal.AbstractGoal;
import me.hatter.tools.cook.goal.CookContext;
import me.hatter.tools.cook.goal.Goal;
import me.hatter.tools.cook.goal.annotation.Name;
import me.hatter.tools.cook.util.FileUtil;
import me.hatter.tools.cook.util.IOUtil;

@Name("project")
public class ProjectGoal extends AbstractGoal {

    @TaskName("create")
    public void runCreateTask(CookContext context) {

        String projectName = context.getInput("project.name", "Please input project name:");

        String build = IOUtil.readResourceToString(ProjectGoal.class, "/templates/build.template.txt");
        build = build.replace("$$project_name$$", projectName);
        FileUtil.writeStringToFile(new File(Goal.USER_DIR, "build.xml"), build);

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
