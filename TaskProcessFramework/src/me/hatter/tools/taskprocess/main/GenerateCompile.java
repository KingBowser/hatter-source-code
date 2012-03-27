package me.hatter.tools.taskprocess.main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.taskprocess.util.env.Env;
import me.hatter.tools.taskprocess.util.misc.FileUtils;
import me.hatter.tools.taskprocess.util.misc.StringUtils;

public class GenerateCompile {

    private String basePath  = Env.getProperty("basepath", Env.USER_DIR);
    private String src       = Env.getProperty("src", "src");
    private String lib       = Env.getProperty("lib", "lib");
    private String classes   = Env.getProperty("classes", "classes");
    private File   fsrc      = new File(basePath, src);
    private File   fjcompile = new File(fsrc, "jcompile");
    private File   flib      = new File(basePath, lib);
    private File   fclasses  = new File(basePath, classes);

    public static void main(String[] args) {
        (new GenerateCompile()).doMain(Env.parseArgs(args));
    }

    protected void doMain(String[] args) {
        checkDirs();
        List<String> libs = listLibs();
        StringWriter jc = new StringWriter();
        PrintWriter pw = new PrintWriter(jc, true);
        pw.println("echo \"[INFO] Listing java file(s) ...\"");
        pw.println("find . -name '*.java' -print > java.list");
        pw.println("echo \"[INFO] Compiling ...\"");
        pw.print("javac");
        if (!libs.isEmpty()) {
            pw.print(" -cp " + StringUtils.join(libs, ":"));
        }
        pw.print(" -d " + fclasses.toString());
        pw.print(" @java.list");
        pw.println();
        pw.println("rm java.list");
        pw.println("echo \"[INFO] Finish!\"");
        pw.flush();
        try {
            FileUtils.writeStringToFile(fjcompile, jc.toString(), Env.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[INFO] File writen to: " + fjcompile);
    }

    protected List<String> listLibs() {
        List<String> libList = new ArrayList<String>();
        if (flib.exists() && flib.isDirectory()) {
            File[] jars = flib.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            });
            for (File j : jars) {
                libList.add(j.toString());
            }
        }
        return libList;
    }

    protected void checkDirs() {
        checkDir(new File(basePath));
        checkDir(fsrc);
        if (!fclasses.exists()) {
            fclasses.mkdir();
        }
    }

    protected void checkDir(File dir) {
        if ((!dir.exists()) || (!dir.isDirectory())) {
            System.out.println("[ERROR] '" + dir + "' not exists or not a directory.");
            System.exit(-1);
        }
    }
}
