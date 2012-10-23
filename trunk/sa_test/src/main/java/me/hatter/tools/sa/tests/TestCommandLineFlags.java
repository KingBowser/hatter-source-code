package me.hatter.tools.sa.tests;

import java.io.PrintStream;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.runtime.VM.Flag;

public class TestCommandLineFlags extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestCommandLineFlags ps = new TestCommandLineFlags();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {
        Flag[] flags = VM.getVM().getCommandLineFlags();
        for (Flag f : flags) {
            System.out.println(f.getName() + " = " + f.getValue() + " [" + f.getType() + ", " + f.getKind() + "]");
        }
    }
}
