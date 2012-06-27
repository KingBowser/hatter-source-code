package me.hatter.tools.cook.util;

public class ConsoleUtil {

    public static String readLie(String prompt) {
        String line;
        do {
            System.out.print(prompt);
            line = System.console().readLine();
        } while (line.trim().length() == 0);
        return line;
    }
}
