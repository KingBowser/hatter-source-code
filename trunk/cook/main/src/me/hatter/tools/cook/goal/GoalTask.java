package me.hatter.tools.cook.goal;

public class GoalTask {

    private String goal;
    private String task;

    public GoalTask(String goal) {
        this.goal = goal;
        this.task = null;
    }

    public GoalTask(String goal, String task) {
        this.goal = goal;
        this.task = task;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
