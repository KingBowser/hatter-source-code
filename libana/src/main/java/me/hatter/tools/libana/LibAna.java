package me.hatter.tools.libana;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.color.Color;
import me.hatter.tools.commons.color.Font;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.JavaWalkTool;
import me.hatter.tools.commons.file.JavaWalkTool.AbstractClassReaderJarWalker;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.io.StringPrintWriter;
import me.hatter.tools.commons.regex.RegexUtil;
import me.hatter.tools.commons.string.StringUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class LibAna {

    abstract public static class AbstractClassNodeReaderJarWalker extends AbstractClassReaderJarWalker<ClassNode> {

        public AbstractClassNodeReaderJarWalker() {
            super(isVerbose(), isTrace());
        }

        protected ClassNode readClass(byte[] bytes) {
            ClassReader cr = new ClassReader(bytes);
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.SKIP_DEBUG);
            return cn;
        }

        protected void dealClass(File jarFile, ClassNode cn, byte[] bytes) {
            String className = cn.name.replace('/', '.');
            dealClassNode(jarFile, cn, className);
        }

        abstract protected void dealClassNode(File jarFile, ClassNode classNode, String className);
    }

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);

        if (UnixArgsutil.ARGS.flags().containsAny("h", "help")) {
            usage();
        }

        String dir = Environment.USER_DIR;
        if (UnixArgsutil.ARGS.args().length > 0) {
            dir = UnixArgsutil.ARGS.args()[0];
        }

        final Set<String> classNameSet = new HashSet<String>();
        final Set<String> duplicatClassNameSet = new HashSet<String>();

        final Pattern filter = getFilter();
        JavaWalkTool tool = new JavaWalkTool(new File(dir));
        System.out.println("Step 1: Analysis duplicate classname list.");
        tool.walk(new AbstractClassNodeReaderJarWalker() {

            @Override
            protected void dealClassNode(File jarFile, ClassNode classNode, String className) {
                if ((filter != null) && !filter.matcher(className).matches()) return;
                if (classNameSet.contains(className)) {
                    duplicatClassNameSet.add(className);
                } else {
                    classNameSet.add(className);
                }
            }
        });

        if (!isVerbose() && isTrace()) System.out.println();
        System.out.println("Find total class count: " + classNameSet.size() + " ,  duplicate class count: "
                           + duplicatClassNameSet.size());

        if (duplicatClassNameSet.size() > 0) {
            System.out.println("Step 2: Analysis diff duplicate classname.");

            final PrintWriter out = new StringPrintWriter();
            final Map<String, ClassNode> classNodeMap = new HashMap<String, ClassNode>();
            final Map<String, File> classJarFileMap = new HashMap<String, File>();

            final Map<String, Set<File>> duplicateClassJarFileMap = new HashMap<String, Set<File>>();
            tool.walk(new AbstractClassNodeReaderJarWalker() {

                @Override
                protected void dealClassNode(File jarFile, ClassNode classNode, String className) {
                    if (duplicatClassNameSet.contains(className)) {
                        if (duplicateClassJarFileMap.get(className) == null) {
                            duplicateClassJarFileMap.put(className, new HashSet<File>());
                        }
                        duplicateClassJarFileMap.get(className).add(jarFile);

                        if (!classNodeMap.containsKey(className)) {
                            classNodeMap.put(className, classNode);
                            classJarFileMap.put(className, jarFile);
                        } else {
                            ClassNode classNode1 = classNodeMap.get(className);
                            ClassNode classNode2 = classNode;
                            File jarFile1 = classJarFileMap.get(className);
                            File jarFile2 = jarFile;

                            diff(className, jarFile1, jarFile2, classNode1, classNode2, out);
                        }
                    }
                }
            });
            if (!isVerbose() && isTrace()) System.out.println();
            System.out.println("Analysis result:");
            System.out.print(out.toString());
            Set<Set<File>> allDuplicateJarSet = new HashSet<Set<File>>(duplicateClassJarFileMap.values());
            System.out.println("All duplicate jars:");
            for (Set<File> duplicateJarSet : allDuplicateJarSet) {
                System.out.println(StringUtil.repeat("-", 100));
                for (File j : duplicateJarSet) {
                    System.out.println("  " + j);
                }
            }
        }
        System.out.println("Analysis finish.");
    }

    private static void diff(String className, File jarFile1, File jarFile2, ClassNode classNode1,
                             ClassNode classNode2, PrintWriter out) {
        Map<String, MethodNode> methodNodeMap1 = methodNodeListToMap(classNode1.methods);
        Map<String, MethodNode> methodNodeMap2 = methodNodeListToMap(classNode2.methods);

        Font clsFont = Font.createFont(isColor() ? Color.getColor(103) : null);
        Font mnsFont = Font.createFont(isColor() ? Color.getColor(101) : null);
        Font addFont = Font.createFont(isColor() ? Color.getColor(102) : null);

        StringPrintWriter writer = new StringPrintWriter();
        for (String methodName : methodNodeMap1.keySet()) {
            MethodNode methodNode1 = methodNodeMap1.get(methodName);
            MethodNode methodNode2 = methodNodeMap2.remove(methodName);

            boolean isMethodPublic1 = (methodNode1.access & Opcodes.ACC_PUBLIC) > 0;

            if ((methodNode2 == null) && (isPublic() ? isMethodPublic1 : true)) {
                writer.println("  " + mnsFont.display("-- " + methodNode1.name + methodNode1.desc));
            } else {
                // writer.println("  == " + methodNode1.name + methodNode1.desc);
            }
        }
        for (MethodNode methodNode2 : methodNodeMap2.values()) {
            boolean isMethodPublic2 = (methodNode2.access & Opcodes.ACC_PUBLIC) > 0;
            if (isPublic() ? isMethodPublic2 : true) {
                writer.println("  " + addFont.display("++ " + methodNode2.name + methodNode2.desc));
            }
        }
        if (writer.getWriter().getBuffer().length() > 0) {
            out.println(clsFont.display("Class: " + className));
            out.println("Jar file1: " + jarFile1);
            out.println("Jar file2: " + jarFile2);
            out.println(writer.toString());
        }
        IOUtil.closeQuitely(writer);
    }

    private static Map<String, MethodNode> methodNodeListToMap(List<MethodNode> methodNodeList) {
        Map<String, MethodNode> methodNodeMap = new LinkedHashMap<String, MethodNode>();
        if (methodNodeList != null) {
            for (MethodNode methodNode : methodNodeList) {
                String methodNameAndDesc = methodNode.name + methodNode.desc;
                methodNodeMap.put(methodNameAndDesc, methodNode);
            }
        }
        return methodNodeMap;
    }

    private static Pattern getFilter() {
        return RegexUtil.createPattern(UnixArgsutil.ARGS.kvalue("filter"), UnixArgsutil.ARGS.flags().contains("i"));
    }

    private static boolean isTrace() {
        return !UnixArgsutil.ARGS.flags().contains("notrace");
    }

    private static boolean isVerbose() {
        return UnixArgsutil.ARGS.flags().contains("verbose");
    }

    private static boolean isColor() {
        return UnixArgsutil.ARGS.flags().contains("color");
    }

    private static boolean isPublic() {
        return UnixArgsutil.ARGS.flags().contains("public");
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar libanaall.jar");
        System.out.println("    -filter             Regex fileter");
        System.out.println("    --i                 Regex ignore case");
        System.out.println("    --h[elp]            Display this message");
        System.out.println("    --verbose           Display verbose message");
        System.out.println("    --notrace           Do not display '.' character per 100 files (when verbose not set)");
        System.out.println("    --color             Color display output");
        System.out.println("    --public            Only compare public methods");
        System.exit(0);
    }
}
