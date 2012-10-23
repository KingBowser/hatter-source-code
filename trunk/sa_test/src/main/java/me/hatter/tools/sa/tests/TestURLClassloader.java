package me.hatter.tools.sa.tests;

import java.io.PrintStream;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.memory.SystemDictionary;
import sun.jvm.hotspot.oops.DefaultOopVisitor;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Klass;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.utilities.Assert;
import sun.jvm.hotspot.utilities.ObjectReader;

public class TestURLClassloader extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestURLClassloader ps = new TestURLClassloader();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {
        SystemDictionary dict = VM.getVM().getSystemDictionary();
        Klass ucl = dict.find("java/net/URLClassLoader", null, null);
        InstanceKlass iucl = (InstanceKlass) ucl;

        System.out.println(iucl);
        iucl.iterate(new DefaultOopVisitor() {

            ObjectReader objReader = new ObjectReader();

            public void doOop(sun.jvm.hotspot.oops.OopField field, boolean isVMField) {
                System.out.println(field.getID().getName());
                if (field.getID().getName().equals("ucp")) {
                    try {
                        System.out.println(isVMField);
                        System.out.println(getObj());
                        System.out.println(objReader.readObject(field.getValue(getObj())));
                    } catch (Exception e) {
                        if (Assert.ASSERTS_ENABLED) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, false);
    }
}
