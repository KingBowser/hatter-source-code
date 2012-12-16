package me.hatter.tools.sa.tests;

import java.io.PrintStream;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.runtime.Arguments;
import sun.jvm.hotspot.runtime.VM;

public class TestDictionary extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestDictionary ps = new TestDictionary();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {
//        VM.getVM().getSystemDictionary();
    }
}
