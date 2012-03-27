package me.hatter.tools.taskprocess.tests;

import java.util.Arrays;

import me.hatter.tools.taskprocess.util.env.Env;

public class EnvTest {

    public static void main(String[] args) {
        String[] as = Env.parseArgs(args);
        System.out.println(Arrays.asList(as));
        for (Object k : System.getProperties().keySet()) {
            System.out.println(k + " = " + System.getProperty((String) k));
        }
    }
}
