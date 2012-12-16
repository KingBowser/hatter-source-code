package me.hatter.tools.sa.tests;

import java.io.PrintStream;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.runtime.Arguments;

public class TestArguments extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestArguments ps = new TestArguments();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(Arguments.getJVMFlags());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(Arguments.getJVMArgs());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(Arguments.getJavaCommand());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
