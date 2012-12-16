package me.hatter.tools.sa.tests;

import java.io.PrintStream;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.oops.HeapVisitor;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Klass;
import sun.jvm.hotspot.oops.Oop;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.utilities.SystemDictionaryHelper;

public class TestClassLoader extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestClassLoader ps = new TestClassLoader();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {
        InstanceKlass k = SystemDictionaryHelper.findInstanceKlass("java.net.URLClassLoader");
        VM.getVM().getObjectHeap().iterateObjectsOfKlass(new HeapVisitor() {

            public void prologue(long l) {
                System.out.println("LLLLLLLLLLLLLLLLL   "+l);
            }

            public void epilogue() {
                System.out.print(".");
            }

            public boolean doObj(Oop o) {
                System.out.println(o.getKlass().getName().asString());
                return false;
            }
        }, k, true);
    }
}
