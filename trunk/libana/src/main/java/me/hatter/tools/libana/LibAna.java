package me.hatter.tools.libana;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.color.Color;
import me.hatter.tools.commons.color.Font;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.JavaWalkTool;
import me.hatter.tools.commons.file.JavaWalkTool.AbstractClassJarJavaWalker;
import me.hatter.tools.commons.file.JavaWalkTool.AcceptType;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.io.StringPrintWriter;
import me.hatter.tools.commons.regex.RegexUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class LibAna {

    abstract public static class AbstractClassReaderJarWalker extends AbstractClassJarJavaWalker {

        private AtomicLong processedCount = new AtomicLong(0);

        public void readInputStream(InputStream is, File file, String name, AcceptType type) {
            processedCount.incrementAndGet();
            if (isVerbose()) {
                if (type == AcceptType.File) {
                    System.out.println("Read file: " + file.getPath());
                }
                if (type == AcceptType.Entry) {
                    System.out.println("Read entry: " + file.getPath() + "!" + name);
                }
            } else if (isTrace() && ((processedCount.get() % 100) == 0)) {
                System.out.print(".");
            }
            byte[] bytes = IOUtil.readToBytesAndClose(new BufferedInputStream(is));
            ClassReader cr = new ClassReader(bytes);
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.SKIP_DEBUG);

            String className = cn.name.replace('/', '.');
            dealClassNode(cn, className);
        }

        abstract protected void dealClassNode(ClassNode classNode, String className);
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
        tool.walk(new AbstractClassReaderJarWalker() {

            @Override
            protected void dealClassNode(ClassNode classNode, String className) {
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
            tool.walk(new AbstractClassReaderJarWalker() {

                @Override
                protected void dealClassNode(ClassNode classNode, String className) {
                    if (duplicatClassNameSet.contains(className)) {
                        if (!classNodeMap.containsKey(className)) {
                            classNodeMap.put(className, classNode);
                        } else {
                            ClassNode classNode1 = classNodeMap.get(className);
                            ClassNode classNode2 = classNode;
                            diff(className, classNode1, classNode2, out);
                        }
                    }
                }
            });
            if (!isVerbose() && isTrace()) System.out.println();
            System.out.println("Analysis result:");
            System.out.print(out.toString());
        }
        System.out.println("Analysis finish.");
    }

    private static void diff(String className, ClassNode classNode1, ClassNode classNode2, PrintWriter out) {
        Map<String, MethodNode> methodNodeMap1 = methodNodeListToMap(classNode1.methods);
        Map<String, MethodNode> methodNodeMap2 = methodNodeListToMap(classNode2.methods);

        Font clsFont = Font.createFont(isColor() ? Color.getColor(103) : null);
        Font mnsFont = Font.createFont(isColor() ? Color.getColor(101) : null);
        Font addFont = Font.createFont(isColor() ? Color.getColor(102) : null);

        StringPrintWriter writer = new StringPrintWriter();
        for (String methodName : methodNodeMap1.keySet()) {
            MethodNode methodNode1 = methodNodeMap1.get(methodName);
            MethodNode methodNode2 = methodNodeMap2.remove(methodName);
            if (methodNode2 == null) {
                writer.println("  " + mnsFont.wrap("-- " + methodNode1.name + methodNode1.desc));
            } else {
                // writer.println("  == " + methodNode1.name + methodNode1.desc);
            }
        }
        for (MethodNode methodNode2 : methodNodeMap2.values()) {
            writer.println("  " + addFont.wrap("++ " + methodNode2.name + methodNode2.desc));
        }
        if (writer.getWriter().getBuffer().length() > 0) {
            out.println(clsFont.wrap("Class: " + className));
            out.println(writer.toString());
        }
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

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar libanaall.jar");
        System.out.println("    -filter             Regex fileter");
        System.out.println("    --i                 Regex ignore case");
        System.out.println("    --h[elp]            Display this message");
        System.out.println("    --verbose           Display verbose message");
        System.out.println("    --notrace           Do not display '.' character per 100 files (when verbose not set)");
        System.out.println("    --color             Color display output");
        System.exit(0);
    }
}
