package me.hatter.tools.sa.tests;

import java.io.PrintStream;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.oops.HeapVisitor;
import sun.jvm.hotspot.oops.ObjectHeap;
import sun.jvm.hotspot.oops.Oop;
import sun.jvm.hotspot.runtime.VM;

public class TestHeapVisitor extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestHeapVisitor ps = new TestHeapVisitor();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {
        ObjectHeap heap = VM.getVM().getObjectHeap();
        heap.iterate(new HeapVisitor() {

            public void prologue(long l) {
            }

            public void epilogue() {
            }

            public boolean doObj(Oop oop) {
                if (oop == null) return false;
                if (oop.getKlass() == null) return false;
                if (oop.getKlass().getName() == null) return false;
                System.out.println(oop.getKlass().getName().asString());
                return false;
            }
        });
    }
}
