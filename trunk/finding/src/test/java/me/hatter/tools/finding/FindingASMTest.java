package me.hatter.tools.finding;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.BasicVerifier;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class FindingASMTest {

    public static void main(final String[] args) throws Exception {
        ClassReader cr = new ClassReader("me.hatter.tools.finding.FindingASMTest");
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_DEBUG);

        System.err.println("CLASS " + cn.name);
        List<MethodNode> methods = cn.methods;
        for (int i = 0; i < methods.size(); ++i) {
            MethodNode method = methods.get(i);
            System.err.println("METHOD " + " " + method.name + method.desc + " " + method.exceptions);
            if (method.instructions.size() > 0) {
                Analyzer<?> a = new Analyzer<BasicValue>(new BasicVerifier());
                try {
                    a.analyze(cn.name, method);
                } catch (Exception ignored) {
                }
                final Frame<?>[] frames = a.getFrames();

                Textifier t = new Textifier() {

                    @Override
                    public void visitMaxs(final int maxStack, final int maxLocals) {
                        for (int i = 0; i < text.size(); ++i) {
                            StringBuffer s = new StringBuffer(frames[i] == null ? "null" : frames[i].toString());
                            while (s.length() < Math.max(20, maxStack + maxLocals + 1)) {
                                s.append(' ');
                            }
                            // System.err.print(Integer.toString(i + 1000).substring(1) + " " + s + " : " +
                            // text.get(i));
                            System.err.print(Integer.toString(i + 10000).substring(1) + " : " + text.get(i));
                        }
                        System.err.println();
                    }
                };
                MethodVisitor mv = new TraceMethodVisitor(t);
                for (int j = 0; j < method.instructions.size(); ++j) {
                    Object insn = method.instructions.get(j);
                    ((AbstractInsnNode) insn).accept(mv);
                }
                mv.visitMaxs(0, 0);
            }
        }
    }
}
