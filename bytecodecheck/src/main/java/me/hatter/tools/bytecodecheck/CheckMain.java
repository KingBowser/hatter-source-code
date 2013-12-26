package me.hatter.tools.bytecodecheck;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CheckMain {

    public static void main(String[] args) throws IOException {

        ClassReader cr = new ClassReader("me.hatter.tools.bytecodecheck.CheckMain");
        ClassDef cd = parseClassDef(cr);
    }

    private static ClassDef parseClassDef(ClassReader cr) {
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        ClassDef classDef = new ClassDef();
        classDef.setName(cn.name);
        classDef.setMethodDefs(new HashSet<MethodDef>());
        // System.out.println(cn.name);

        List<MethodNode> mns = cn.methods;
        // System.out.println("Method count; " + mns.size());
        for (MethodNode mn : mns) {
            MethodDef methodDef = new MethodDef();
            methodDef.setName(mn.name);
            methodDef.setDesc(mn.desc);
            methodDef.setMethodInvokes(new HashSet<MethodInvoke>());
            classDef.getMethodDefs().add(methodDef);
            // System.out.println(Modifier.toString(mn.access));
            // System.out.println(Arrays.asList(mn.name, mn.desc, mn.signature));

            InsnList il = mn.instructions;
            for (int i = 0; i < il.size(); i++) {
                AbstractInsnNode in = il.get(i);
                if (in instanceof MethodInsnNode) {
                    MethodInvoke methodInvoke = new MethodInvoke();
                    methodInvoke.setClazz(((MethodInsnNode) in).owner);
                    methodInvoke.setMethod(((MethodInsnNode) in).name);
                    methodInvoke.setDesc(((MethodInsnNode) in).desc);
                    methodDef.getMethodInvokes().add(methodInvoke);
                    // System.out.println("    "
                    // + Arrays.asList(((MethodInsnNode) in).owner, ((MethodInsnNode) in).name,
                    // ((MethodInsnNode) in).desc));
                } else if (in instanceof InvokeDynamicInsnNode) {
                    // IGNORE invoke dynamic
                    // System.out.println("    XXXX " + Arrays.asList(in.getClass(), in));
                }
            }
        }
        return classDef;
    }
}
