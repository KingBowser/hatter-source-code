package me.hatter.tools.sa;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import sun.jvm.hotspot.runtime.VM;

public class XMain {

    public static void main(String[] args) {
        Class vm = VM.class;
        Method[] ms = vm.getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().contains("access$")) continue;
            if (m.getParameterTypes().length > 0) continue;
            if (!Modifier.isPublic( m.getModifiers())) continue;
            System.out.println("| `" +m.getName() + "` | `" + m.getReturnType().getName() + "` |  |");
        }
    }
}
