package me.hatter.tools.sa.tests;

import java.io.PrintStream;
import java.util.Iterator;

import me.hatter.tools.sa.Tool;
import sun.jvm.hotspot.HotSpotTypeDataBase;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.types.Type;
import sun.jvm.hotspot.types.basic.BasicField;

public class TestDatabaseType extends Tool {

    public static void main(String[] args, PrintStream err) {
        TestDatabaseType ps = new TestDatabaseType();
        ps.start(args, err);
        ps.stop();
    }

    public void run() {
        HotSpotTypeDataBase db = (HotSpotTypeDataBase) VM.getVM().getTypeDataBase();
        {
            Iterator<Type> it = db.getTypes();
            while (it.hasNext()) {
                Type t = it.next();
                System.out.println(t.getName());
                Iterator fit = t.getFields();
                while (fit.hasNext()) {
                    BasicField f = (BasicField) fit.next();
                    System.out.println("    " + f.getName());
                }
            }
        }
        // System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // {
        // Iterator it = db.getIntConstants();
        // while (it.hasNext()) {
        // Object i = it.next();
        // System.out.println(i);
        // }
        // }
        // System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // {
        // Iterator it = db.getLongConstants();
        // while (it.hasNext()) {
        // Object i = it.next();
        // System.out.println(i);
        // }
        // }
    }
}
