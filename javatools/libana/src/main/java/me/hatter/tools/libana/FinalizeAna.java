package me.hatter.tools.libana;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.JavaWalkTool;
import me.hatter.tools.libana.LibAna.AbstractClassNodeReaderJarWalker;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class FinalizeAna {

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);

        String dir = Environment.USER_DIR;
        if (UnixArgsutil.ARGS.args().length > 0) {
            dir = UnixArgsutil.ARGS.args()[0];
        }
        StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        JavaWalkTool tool = new JavaWalkTool(new File(dir));
        tool.walk(new AbstractClassNodeReaderJarWalker() {

            @Override
            protected void dealClassNode(File jarFile, ClassNode classNode, String className) {
                List<MethodNode> methods = classNode.methods;
                for (MethodNode method : methods) {
                    if ("finalize".equals(method.name) && "()V".equals(method.desc)) {
                        pw.println("[INFO] Found finalize()V: " + classNode.name.replace('/', '.'));
                    }
                }
            }
        });
        System.out.println();
        System.out.print(sw.toString());
    }
}
