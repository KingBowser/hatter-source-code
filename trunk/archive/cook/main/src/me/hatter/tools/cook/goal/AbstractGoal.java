package me.hatter.tools.cook.goal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import me.hatter.tools.cook.goal.annotation.Name;

public abstract class AbstractGoal implements Goal {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface TaskName {

        String value();
    }

    protected Map<String, Method> taskMethodMap = new HashMap<String, Method>();

    public AbstractGoal() {
        Class<?> clazz = this.getClass();
        while (clazz != Object.class) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                TaskName taskName = m.getAnnotation(TaskName.class);
                if (taskName != null) {
                    if ((m.getParameterTypes().length == 1) && (m.getParameterTypes()[0] == CookContext.class)) {
                        if (taskMethodMap.containsKey(taskName)) {
                            System.out.println("[INFO] Task " + taskName.value()
                                               + " was overrided, so the implement in: " + clazz.getName()
                                               + " is ignored.");
                        } else {
                            m.setAccessible(true);
                            taskMethodMap.put(taskName.value().toLowerCase(), m);
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    @Override
    public void run(CookContext context, String task) {
        Method m = taskMethodMap.get(task.trim().toLowerCase());
        if (m != null) {
            try {
                m.invoke(this, new Object[] { context });
                System.out.println("[INFO] Run finish: " + this.getClass().getAnnotation(Name.class).value() + ":"
                                   + m.getAnnotation(TaskName.class).value());
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Cannot find task method: " + task + ", clazz: " + this.getClass());
    }
}
