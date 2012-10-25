package me.hatter.tools.bigpom;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.commons.xml.XmlParser;

public class BigPom {

    public static void main(String[] args) throws Exception {
        List<File> pomList = listPomFiles(new File(Environment.USER_DIR));
        if (pomList.isEmpty()) {
            System.out.println("No pom.xml file found.");
            return;
        }
        System.out.println("Found pom.xml files:");
        for (File f : pomList) {
            System.out.println("  " + f);
        }
        Version maxVersion = null;
        List<String> filterProjectList = new ArrayList<String>();
        for (File f : pomList) {
            try {
                XmlParser xp = new XmlParser(new FileInputStream(f));
                String parentArtifactId = xp.parseXpathNode("/project/parent/artifactId").getTextContent();
                if (!("intl.base".equalsIgnoreCase(parentArtifactId.trim()))) {
                    continue;
                }
                String parentVersion = xp.parseXpathNode("/project/parent/version").getTextContent();
                filterProjectList.add(f.getParent());
                Version version = Version.parse(parentVersion);
                if (maxVersion == null) {
                    maxVersion = version;
                } else {
                    if (maxVersion.compareTo(version) < 0) {
                        maxVersion = version;
                    }
                }
            } catch (RuntimeException e) {
                System.out.println("Read file: " + f + " failed, for: " + e.getMessage());
                throw e;
            }
        }
        if (filterProjectList.isEmpty()) {
            System.out.println("No project path found.");
            return;
        }
        List<String> moduleList = new ArrayList<String>();
        System.out.println("Max intl.base version: " + maxVersion);
        System.out.println("Found project paths:");
        for (String p : filterProjectList) {
            moduleList.add("<module>" + p.replace(Environment.USER_DIR, "../") + "</module>");
            System.out.println("  " + p.replace(Environment.USER_DIR, "../"));
        }
        String t = IOUtil.readResourceToString(BigPom.class, "/pom.xml.template");
        t = t.replace("${BASE_POM_VERSION}", maxVersion.toString());
        t = t.replace("${MODULES_LIST}", StringUtil.join(moduleList, "\n"));
        File bigPom = new File(Environment.USER_DIR, "bigpom");
        bigPom.mkdirs();
        File bigPomXml = new File(bigPom, "pom.xml");
        FileUtil.writeStringToFile(bigPomXml, t);

        List<String> cmds = new ArrayList<String>();
        cmds.add("cd bigpom");
        cmds.add("mvn clean install -Dmaven.test.skip");
        cmds.add("cd ..");
        FileUtil.writeStringToFile(new File("compilebigpom"), StringUtil.join(cmds, "\n"));
        new ProcessBuilder(Arrays.asList("chmod", "+x", "compilebigpom")).start();

        System.out.println("Output big pom: " + bigPomXml);
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
                if (pathname.getName().equals("bigpom")) {
                    return false;
                }
                if (pathname.isDirectory()) {
                    File[] files = pathname.listFiles();
                    for (File f : files) {
                        if (f.getName().equals("all")) {
                            return false;
                        }
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
