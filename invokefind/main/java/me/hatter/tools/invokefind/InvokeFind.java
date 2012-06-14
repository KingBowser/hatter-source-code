package me.hatter.tools.invokefind;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.file.JavaUtil;
import me.hatter.tools.commons.file.JavaUtil.JavaWalker;
import me.hatter.tools.commons.io.IOUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class InvokeFind {

    private static Set<String>  INVOKES = new HashSet<String>(Arrays.asList("INVOKEVIRTUAL", "INVOKESPECIAL",
                                                                            "INVOKESTATIC", "INVOKEINTERFACE"));

    private static List<String> FOUNDS  = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        UnixArgsutil.parseGlobalArgs(args);
        if (UnixArgsutil.ARGS.args().length == 0) {
            System.out.println("[ERROR] No args assiged.");
            System.out.println("Usage:");
            System.out.println("  invokefind [flags] <args>");
            System.out.println("  java -jar invokefind [flags] <args>");
            System.out.println("  java -cp invokefind.jar invokefind [flags] <args>");
            System.out.println("    -d <dir>       target dir[default user.dir]");
            System.out.println("    --vf           print visit file");
            System.out.println("    --vc           print visit class");
            System.out.println("    --vm           print visit method");
            System.out.println("    --noins        print no instructions methods");
            System.out.println("Sample:");
            System.out.println("  invokefind --vc xstream.<init> string.intern");

            System.exit(-1);
        }
        System.out.println("[INFO] Args: " + Arrays.asList(UnixArgsutil.ARGS.args()));

        final List<String> clsmes = CollectionUtil.toLowerCase(Arrays.asList(UnixArgsutil.ARGS.args()));

        String d = UnixArgsutil.ARGS.kvalue("d", System.getProperty("user.dir"));
        System.out.println("[INFO] Dir: " + d);

        JavaUtil.walk(new File(d), true, new JavaWalker() {

            public void read(InputStream is, String name) {
                byte[] bytes = IOUtil.readToBytesAndClose(is);
                ClassReader cr = new ClassReader(bytes);
                visitClassReader(cr, clsmes);
            }

            public void read(File file) {
                if (!file.toString().endsWith(".class")) {
                    return;
                }
                if (UnixArgsutil.ARGS.flags().contains("vf")) {
                    System.out.println("[INFO] Visit file: " + file);
                }
                byte[] bytes = FileUtil.readFileToBytes(file);
                ClassReader cr = new ClassReader(bytes);
                visitClassReader(cr, clsmes);
            }

            public boolean accept(JarEntry jarEntry) {
                if (UnixArgsutil.ARGS.flags().contains("vf")) {
                    System.out.println("[INFO] Visit jar file: " + jarEntry.getName());
                }
                return jarEntry.getName().endsWith(".class");
            }

            public boolean accept(File dir) {
                return (!dir.toString().contains(".svn"));
            }
        });

        System.out.println();
        System.out.println("[INFO] Walk finish!");
        for (String f : FOUNDS) {
            System.out.println(f);
        }
    }

    private static void visitClassReader(ClassReader cl, final List<String> clsmes) {
        final ClassNode cn = new ClassNode();
        cl.accept(cn, ClassReader.SKIP_DEBUG);
        if (UnixArgsutil.ARGS.flags().contains("vc")) {
            System.out.println("[INFO] Visit class: " + cn.name);
        }

        List<MethodNode> methods = cn.methods;
        for (final MethodNode mn : methods) {
            if (mn.instructions.size() == 0) {
                if (UnixArgsutil.ARGS.flags().contains("noins")) {
                    System.out.println("[INFO] No instructions, method: " + cn.name + "#" + mn.name + mn.desc);
                }
            } else {
                if (UnixArgsutil.ARGS.flags().contains("vm")) {
                    System.out.println("[INFO] Visit method: " + cn.name + "#" + mn.name + mn.desc);
                }

                Textifier t = new Textifier() {

                    @Override
                    public void visitMaxs(final int maxStack, final int maxLocals) {
                        for (int i = 0; i < text.size(); ++i) {
                            String c = text.get(i).toString().trim();
                            if (c.contains(" ")) {
                                String[] cs = c.split("\\s+");
                                String op = cs[0];
                                String cm = cs[1].toLowerCase().replace('/', '.').replace('$', '.');
                                if (INVOKES.contains(op)) {
                                    for (String clsm : clsmes) {
                                        if (cm.contains(clsm)) {
                                            String found = "[INFO] Match found: [" + cn.name + "#" + mn.name + mn.desc
                                                           + "]:  " + c;
                                            System.out.println(found);
                                            FOUNDS.add(found);
                                        }
                                    }
                                }
                            }
                        }
                    }
                };
                MethodVisitor mv = new TraceMethodVisitor(t);
                for (int j = 0; j < mn.instructions.size(); ++j) {
                    Object insn = mn.instructions.get(j);
                    ((AbstractInsnNode) insn).accept(mv);
                }
                mv.visitMaxs(0, 0);
            }
        }
    }
}
