package me.hatter.tools.sa.tests;

import java.io.PrintStream;
import java.util.Properties;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.oops.DefaultOopVisitor;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.utilities.Assert;
import sun.jvm.hotspot.utilities.ObjectReader;

public class TestSystemProperties extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestSystemProperties ps = new TestSystemProperties();
        ps.start(args, err);
        ps.stop();
    }

    Properties sysProps;

    @SuppressWarnings("static-access")
    public void run() {
        InstanceKlass systemKls = VM.getVM().getSystemDictionary().getSystemKlass();
        systemKls.iterate(new DefaultOopVisitor() { // only static field

            ObjectReader objReader = new ObjectReader();

            public void doOop(sun.jvm.hotspot.oops.OopField field, boolean isVMField) {
                if (field.getID().getName().equals("props")) {
                    try {
                        sysProps = (Properties) objReader.readObject(field.getValue(getObj()));
                    } catch (Exception e) {
                        if (Assert.ASSERTS_ENABLED) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, false);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        for (Object k : sysProps.keySet()) {
            System.out.println(k + ":" + sysProps.get(k));
        }
        System.out.println(sysProps);
    }
}
