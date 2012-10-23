package me.hatter.tools.sa.tests;

import java.io.PrintStream;

import me.hatter.tools.sa.Tool;

public class TestSystemProperties extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestSystemProperties ps = new TestSystemProperties();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {

    }
}
