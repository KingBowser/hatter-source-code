package me.hatter.tools.bigpom;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;

public class Main {

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);

        List<File> pomList = listPomFiles(new File(Environment.USER_DIR));
        if (pomList.isEmpty()) {
            System.out.println("No pom.xml file found.");
            return;
        }
        System.out.println("Found pom.xml files:");
        for (File f : pomList) {
            System.out.println("  " + f);
        }
    }

    private static List<File> listPomFiles(File dir) {

        final AtomicLong count = new AtomicLong(0);
        final List<File> pomList = new ArrayList<File>();
        FileUtil.listFiles(dir, new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.toString().contains(".svn")) {
                    return false;
                }
                count.incrementAndGet();
                if (count.get() % 100 == 0) {
                    System.out.print(".");
                }
                if (pathname.isDirectory()) {
                    File[] files = pathname.listFiles();
                    for (File f : files) {
                        if (f.getName().equals("pom.xml")) {
                            String pom = FileUtil.readFileToString(f);
                            if (!pom.contains("(BIG POM)")) {
                                pomList.add(f);
                                return false;
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        }, null);
        System.out.println();
        return pomList;
    }
}
