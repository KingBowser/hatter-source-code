package me.hatter.tools.sa.tests;

import java.io.PrintStream;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.memory.SystemDictionary;
import sun.jvm.hotspot.memory.SystemDictionary.ClassAndLoaderVisitor;
import sun.jvm.hotspot.oops.Klass;
import sun.jvm.hotspot.oops.Oop;
import sun.jvm.hotspot.runtime.VM;

public class TestKlass extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestKlass ps = new TestKlass();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {
        SystemDictionary dict = VM.getVM().getSystemDictionary();
        System.out.println("============================================");
        dict.classesDo(new ClassAndLoaderVisitor() {

            public void visit(Klass k, Oop o) {
                System.out.println(k.getClass().getSimpleName() + ": " + k.getName().asString());
                if (o == null) {
                    System.out.println("  >>>> " + o);
                } else {
                    System.out.println("  >>>> " + o.getKlass().getName().asString());
                }
            }
        });
        // System.out.println("============================================");
        // dict.classesDo(new SystemDictionary.ClassVisitor() {
        //
        // public void visit(Klass k) {
        // System.out.println(k.getClass().getSimpleName() + ": " + k.getName().asString());
        // // if (k instanceof InstanceKlass) {
        // // InstanceKlass ik = (InstanceKlass) k;
        // // }
        // }
        // });
    }
}
