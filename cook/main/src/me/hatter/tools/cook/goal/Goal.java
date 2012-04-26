package me.hatter.tools.cook.goal;

public interface Goal {

    public static final String USER_DIR  = System.getProperty("user.dir");
    public static final String USER_HOME = System.getProperty("user.home");

    void run(CookContext context, String task);
}
