package me.hatter.tools.invokefind;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
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
import me.hatter.tools.commons.io.FilePrintWriter;
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
            System.out.println("    -t <dir>       target dir[default user.dir]");
            System.out.println("    -o <file>      output file");
            System.out.println("    -ig <str>      ignore[contains]");
            System.out.println("    -igc <str>     ignore class[contains]");
            System.out.println("    -igm <str>     ignore method[equals]");
            System.out.println("    --vf           print visit file");
            System.out.println("    --vc           print visit class");
            System.out.println("    --vm           print visit method");
            System.out.println("    --vs           print summary");
            System.out.println("    --noins        print no instructions methods");
            System.out.println("Sample:");
            System.out.println("  invokefind --vc \"xstream.<init>\" string.intern");

            System.exit(-1);
        }
        if (UnixArgsutil.ARGS.kvalues("ig") != null) {
            System.out.println("[INFO] Ignore: " + UnixArgsutil.ARGS.kvalues("ig"));
        }
        if (UnixArgsutil.ARGS.kvalues("igc") != null) {
            System.out.println("[INFO] Ignore class: " + UnixArgsutil.ARGS.kvalues("igc"));
        }
        if (UnixArgsutil.ARGS.kvalues("igm") != null) {
            System.out.println("[INFO] Ignore method: " + UnixArgsutil.ARGS.kvalues("igm"));
        }
        System.out.println("[INFO] Args: " + Arrays.asList(UnixArgsutil.ARGS.args()));

        final List<String> clsmes = CollectionUtil.toLowerCase(Arrays.asList(UnixArgsutil.ARGS.args()));

        String d = UnixArgsutil.ARGS.kvalue("t", System.getProperty("user.dir"));
        System.out.println("[INFO] Target: " + d);

        String f = UnixArgsutil.ARGS.kvalue("o");
        PrintWriter PW = null;
        if (f != null) {
            File ff = new File(f);
            if (ff.exists()) {
                System.out.println("[INFO] File exists: " + f);
                System.exit(-1);
            }
            PW = new FilePrintWriter(ff);
            System.out.println("[INFO] Output: " + ff.getAbsolutePath().toString());
        }
        final PrintWriter PPW = PW;

        JavaUtil.walk(new File(d), true, new JavaWalker() {

            public void read(InputStream is, String name) {
                byte[] bytes = IOUtil.readToBytesAndClose(is);
                ClassReader cr = new ClassReader(bytes);
                visitClassReader(cr, clsmes, PPW);
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
                visitClassReader(cr, clsmes, PPW);
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
        if (UnixArgsutil.ARGS.flags().contains("vs")) {
            for (String fo : FOUNDS) {
                System.out.println(fo);
            }
        }
        IOUtil.closeQuitely(PPW);
    }

    private static void visitClassReader(ClassReader cl, final List<String> clsmes, final PrintWriter pw) {
        final ClassNode cn = new ClassNode();
        cl.accept(cn, ClassReader.SKIP_DEBUG);
        if (UnixArgsutil.ARGS.flags().contains("vc")) {
            System.out.println("[INFO] Visit class: " + cn.name);
        }

        String lcn = cn.name.replace('/', '.').replace('$', '.').toLowerCase();
        if (UnixArgsutil.ARGS.kvalues("igc") != null) {
            for (String igc : UnixArgsutil.ARGS.kvalues("igc")) {
                if (lcn.contains(igc)) {
                    return;
                }
            }
        }

        List<MethodNode> methods = cn.methods;
        FOR_METHOD: for (final MethodNode mn : methods) {

            String lmn = mn.name.toLowerCase();
            if (UnixArgsutil.ARGS.kvalues("igm") != null) {
                for (String igm : UnixArgsutil.ARGS.kvalues("igm")) {
                    if (lmn.equals(igm)) {
                        continue FOR_METHOD;
                    }
                }
            }
            if (UnixArgsutil.ARGS.kvalues("ig") != null) {
                for (String igm : UnixArgsutil.ARGS.kvalues("ig")) {
                    if ((lcn + "." + lmn).equals(igm)) {
                        continue FOR_METHOD;
                    }
                }
            }

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
                                            String found = "[INFO] Match found: [" + cn.name + "." + mn.name + mn.desc
                                                           + "]:  " + c;
                                            System.out.println(found);
                                            if (pw != null) {
                                                pw.println(found);
                                            }
                                            if (UnixArgsutil.ARGS.flags().contains("vs")) {
                                                FOUNDS.add(found);
                                            }
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
