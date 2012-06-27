package me.hatter.tools.cook.goal.impl;

import java.io.File;

import me.hatter.tools.cook.goal.AbstractGoal;
import me.hatter.tools.cook.goal.CookContext;
import me.hatter.tools.cook.goal.Goal;
import me.hatter.tools.cook.goal.annotation.Name;
import me.hatter.tools.cook.util.FileUtil;
import me.hatter.tools.cook.util.IOUtil;

@Name("eclipse")
public class EclipseGoal extends AbstractGoal {

    @TaskName("clean")
    public void runCleanTask(CookContext context) {
        String[] files = new String[] { ".projet", ".classpath" };

        for (String file : files) {
            File f = new File(Goal.USER_DIR, file);
            if (f.exists()) {
                System.out.print("[INFO] Delete " + file);
                f.delete();
            }
        }
    }

    @TaskName("eclipse")
    public void runEclipseTask(CookContext context) {
        String projectName = context.getInput("project.name", "Please input project name:");

        System.out.println("[INFO] Write .classpath");
        String classpath = IOUtil.readResourceToString(EclipseGoal.class, "/templates/classpath.template.txt");
        classpath = classpath.replace("$$project_name$$", projectName);
        FileUtil.writeStringToFile(new File(Goal.USER_DIR, ".classpath"), classpath);

        System.out.println("[INFO] Write .project");
        String project = IOUtil.readResourceToString(EclipseGoal.class, "/templates/project.template.txt");
        project = project.replace("$$project_name$$", projectName);
        FileUtil.writeStringToFile(new File(Goal.USER_DIR, ".project"), project);
    }
}
