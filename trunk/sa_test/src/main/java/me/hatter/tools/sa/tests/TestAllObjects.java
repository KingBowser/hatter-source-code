package me.hatter.tools.sa.tests;

import java.io.PrintStream;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.oops.HeapVisitor;
import sun.jvm.hotspot.oops.Oop;
import sun.jvm.hotspot.runtime.VM;

public class TestAllObjects extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestAllObjects ps = new TestAllObjects();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {
        VM.getVM().getObjectHeap().iterate(new HeapVisitor() {

            long ocount = 0;
            long count  = 0;

            public void prologue(long l) {
                this.ocount = l;
            }

            public void epilogue() {
                System.out.println();
                System.out.println(">>>END>>> " + ocount + " : " + count);
            }

            public boolean doObj(Oop o) {
                count++;
                if (count % 10000 == 0) System.out.print(".");
                return false;
            }
        });
    }
}
