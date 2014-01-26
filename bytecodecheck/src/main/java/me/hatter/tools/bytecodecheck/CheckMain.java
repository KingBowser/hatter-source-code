package me.hatter.tools.bytecodecheck;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarInputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CheckMain {

    private static final String MATCHING_CLASS_NAME_PREFIX = "com/" + "a" + "l" + "i" + "b" + "a" + "b" + "a/";

    public static void main(String[] args) throws IOException {
        // UnixArgsutil.parseGlobalArgs(args);

        String tarFile = "/Users/hatterjiang/temp/ttt/bss__.tar";
        Map<String, List<ClassDef>> allClassDefMap = parseTar(tarFile);

        for (String className : allClassDefMap.keySet()) {
            if (allClassDefMap.get(className).size() != 1) {
                System.out.println(className + ":" + allClassDefMap.get(className).size());
            }
        }
    }

    private static void tryMatch(Map<String, List<ClassDef>> allClassDefMap, ClassDef classDef) {
        for (MethodDef methodDef : classDef.getMethodDefs()) {
            for (MethodInvoke methodInvoke : methodDef.getMethodInvokes()) {
                // TODO
            }
        }
    }

    private static Map<String, List<ClassDef>> parseTar(String tarFile) throws FileNotFoundException, IOException {
        Map<String, List<ClassDef>> allClassDefMap = new HashMap<String, List<ClassDef>>();
        TarInputStream tis = new TarInputStream(new BufferedInputStream(new FileInputStream(tarFile)));
        try {
            for (TarEntry tarEntry; ((tarEntry = tis.getNextEntry()) != null);) {
                String entryName = tarEntry.getName();
                if (!entryName.endsWith(".jar")) {
                    continue;
                }
                byte data[] = new byte[2048];
                ByteArrayOutputStream tbaos = new ByteArrayOutputStream();
                for (int count; (count = tis.read(data)) != -1;) {
                    tbaos.write(data, 0, count);
                }
                tbaos.flush();
                byte[] jarBytes = tbaos.toByteArray();
                tbaos.close();

                // System.out.println("Entry name: " + entryName);

                ByteArrayInputStream bais = new ByteArrayInputStream(jarBytes);
                ZipInputStream zis = new ZipInputStream(bais);
                for (ZipEntry ze; ((ze = zis.getNextEntry()) != null);) {
                    if (!ze.getName().endsWith(".class")) {
                        continue;
                    }
                    ByteArrayOutputStream jbaos = new ByteArrayOutputStream();
                    for (int count; (count = zis.read(data)) != -1;) {
                        jbaos.write(data, 0, count);
                    }
                    jbaos.flush();
                    byte[] classBytes = jbaos.toByteArray();
                    jbaos.close();

                    try {
                        ClassReader cr = new ClassReader(classBytes);
                        ClassDef classDef = parseClassDef(cr, entryName);
                        if (!classDef.getName().startsWith(MATCHING_CLASS_NAME_PREFIX)) {
                            continue;
                        }
                        // System.out.println("   :::::" + classDef.getName());
                        if (allClassDefMap.get(classDef.getName()) == null) {
                            allClassDefMap.put(classDef.getName(), new ArrayList<ClassDef>());
                        }
                        allClassDefMap.get(classDef.getName()).add(classDef);
                    } catch (Exception e) {
                        System.out.println("[ERROR] Parse failed:" + ze.getName());
                    }
                }
            }
        } finally {
            tis.close();
        }
        return allClassDefMap;
    }

    private static ClassDef parseClassDef(ClassReader cr, String refJar) {
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        ClassDef classDef = new ClassDef();
        classDef.setRefJar(refJar);
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
