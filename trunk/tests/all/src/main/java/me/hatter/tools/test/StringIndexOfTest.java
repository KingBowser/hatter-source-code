package me.hatter.tools.test;

public class StringIndexOfTest {

    public static void main(String[] args) {
        for (int i = 0; i < 1000000000; i++) {
            if ("abcdef".indexOf("cd") < 0) {
                System.err.println("ERROR!");
            }
        }
    }
}
