package me.hatter.tools.libsdiff;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.file.JavaWalkTool;
import me.hatter.tools.commons.file.JavaWalkTool.AbstractClassReaderJarWalker;
import me.hatter.tools.commons.misc.Base64;
import me.hatter.tools.commons.string.StringUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class LibsDiff {

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);

        String p1 = UnixArgsutil.ARGS.kvalue("p1");
        String p2 = UnixArgsutil.ARGS.kvalue("p2");

        if ((StringUtil.isBlank(p1)) || StringUtil.isEmpty(p2)) {
            usage();
        }
        File fp1 = new File(p1);
        File fp2 = new File(p2);
        boolean hasError = false;
        if (fp1.exists() && fp1.isDirectory()) {
        } else {
            hasError = true;
            System.out.println("-p1 '" + p1 + "' not exists or not a directory");
        }
        if (fp2.exists() && fp2.isDirectory()) {
        } else {
            hasError = true;
            System.out.println("-p2 '" + p2 + "' not exists or not a directory");
        }
        if (hasError) {
            System.exit(-2);
        }
        JavaWalkTool tool1 = new JavaWalkTool(fp1);
        ClassAndMd5Walker walker1 = new ClassAndMd5Walker();
        tool1.walk(walker1);

        JavaWalkTool tool2 = new JavaWalkTool(fp2);
        ClassAndMd5Walker walker2 = new ClassAndMd5Walker();
        tool2.walk(walker2);

        Map<String, List<ClassJarMd5>> map1 = walker1.getClassNameJarMd5Map();
        Map<String, List<ClassJarMd5>> map2 = walker2.getClassNameJarMd5Map();

        Set<String> onlySet1 = new HashSet<String>(map1.keySet());
        onlySet1.removeAll(map2.keySet());
        Set<String> onlySet2 = new HashSet<String>(map2.keySet());
        onlySet2.removeAll(map1.keySet());

        System.out.println();
        if (!onlySet1.isEmpty()) {
            System.out.println("[Only in p1:]");
            for (String c : onlySet1) {
                List<ClassJarMd5> md5s = map1.get(c);
                System.out.println("" + c);
                for (ClassJarMd5 md5 : md5s) {
                    System.out.println("    " + md5.getMd5() + "  " + md5.getJar());
                }
            }
        }
        if (!onlySet2.isEmpty()) {
            System.out.println("[Only in p2:]");
            for (String c : onlySet2) {
                List<ClassJarMd5> md5s = map2.get(c);
                System.out.println("" + c);
                for (ClassJarMd5 md5 : md5s) {
                    System.out.println("    " + md5.getMd5() + "  " + md5.getJar());
                }
            }
        }
        System.out.println("[In both path:]");
        for (String c : map1.keySet()) {
            List<ClassJarMd5> ms1 = map1.get(c);
            List<ClassJarMd5> ms2 = map2.get(c);
            if (ms2 == null) continue;
            diffMs(c, ms1, ms2);
        }
    }

    private static void diffMs(String c, List<ClassJarMd5> ms1, List<ClassJarMd5> ms2) {
        Map<String, ClassJarMd5> m1 = toMap(ms1);
        Map<String, ClassJarMd5> m2 = toMap(ms2);
        Set<String> os1 = new HashSet<String>(m1.keySet());
        os1.removeAll(m2.keySet());
        Set<String> os2 = new HashSet<String>(m2.keySet());
        os2.removeAll(m1.keySet());
        if (os1.isEmpty() && os2.isEmpty()) return;
        System.out.println("" + c);
        if (!os1.isEmpty()) {
            for (String o1 : os1) {
                ClassJarMd5 md5 = m1.get(o1);
                System.out.println("    --  " + md5.getMd5() + "  " + md5.getJar());
            }
        }
        if (!os2.isEmpty()) {
            for (String o2 : os2) {
                ClassJarMd5 md5 = m2.get(o2);
                System.out.println("    ++  " + md5.getMd5() + "  " + md5.getJar());
            }
        }
    }

    private static Map<String, ClassJarMd5> toMap(List<ClassJarMd5> ms) {
        Map<String, ClassJarMd5> map = new HashMap<String, LibsDiff.ClassJarMd5>();
        for (ClassJarMd5 md5 : ms) {
            map.put(md5.getMd5(), md5);
        }
        return map;
    }

    public static class ClassJarMd5 {

        private String className;
        private String jar;
        private String md5;

        public ClassJarMd5(String className, String jar, String md5) {
            super();
            this.className = className;
            this.jar = jar;
            this.md5 = md5;
        }

        public String getClassName() {
            return className;
        }

        public String getJar() {
            return jar;
        }

        public String getMd5() {
            return md5;
        }
    }

    public static class ClassAndMd5Walker extends AbstractClassReaderJarWalker<ClassNode> {

        private Map<String, List<ClassJarMd5>> classNameJarMd5Map = new HashMap<String, List<ClassJarMd5>>();

        public ClassAndMd5Walker() {
            super(UnixArgsutil.ARGS.flags().contains("verbose"), UnixArgsutil.ARGS.flags().contains("trace"));
        }

        public Map<String, List<ClassJarMd5>> getClassNameJarMd5Map() {
            return classNameJarMd5Map;
        }

        @Override
        protected ClassNode readClass(byte[] bytes) {
            ClassReader cr = new ClassReader(bytes);
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.SKIP_DEBUG);
            return cn;
        }

        @Override
        protected void dealClass(File jarFile, ClassNode cn, byte[] bytes) {
            String md5b64;
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(bytes);
                md5b64 = Base64.byteArrayToBase64(md5.digest());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            String className = cn.name;
            String jar = (jarFile == null) ? null : jarFile.getName();
            ClassJarMd5 cmd5 = new ClassJarMd5(className, jar, md5b64);
            if (classNameJarMd5Map.get(className) == null) {
                classNameJarMd5Map.put(className, new ArrayList<LibsDiff.ClassJarMd5>());
            }
            classNameJarMd5Map.get(className).add(cmd5);
        }
    }

    private static void usage() {
        System.out.println("java -jar xxx.jar -p1 lib1 -p2 lib2");
        System.exit(-1);
    }
}
