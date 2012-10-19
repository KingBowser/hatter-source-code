package me.hatter.tools.classdump;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.jvm.HotSpotProcessUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKLib;
import me.hatter.tools.commons.jvm.HotSpotVMUtil.JDKTarget;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.map.CountingMap;
import me.hatter.tools.commons.regex.RegexUtil;
import sun.jvm.hotspot.memory.SystemDictionary;
import sun.jvm.hotspot.oops.Instance;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Klass;
import sun.jvm.hotspot.oops.Oop;
import sun.jvm.hotspot.oops.OopField;
import sun.jvm.hotspot.oops.OopUtilities;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.jcore.ClassWriter;

/**
 * Some code is copied from: https://gist.github.com/1441625
 * 
 * @author hatterjiang
 */
public class ClassDump {

    public static void main(String[] args) {
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.TOOLS);
        HotSpotVMUtil.autoAddToolsJarDependency(JDKTarget.SYSTEM_CLASSLOADER, JDKLib.SA_JDI);
        UnixArgsutil.parseGlobalArgs(args);

        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }
        if (UnixArgsutil.ARGS.kvalue("filter") == null) {
            LogUtil.error("Args -filter <class name regex> MUST assigned, if dump all classes set to '.*'");
            System.exit(0);
        }

        ClassDumpTool.main(new String[] { UnixArgsutil.ARGS.args()[0] }, System.out);
    }

    public static class ClassDumpTool extends Tool {

        private Pattern     pattern         = RegexUtil.createPattern(UnixArgsutil.ARGS.kvalue("filter"),
                                                                      UnixArgsutil.ARGS.flags().contains("i"));
        private String      outputDirectory = UnixArgsutil.ARGS.kvalue("output", ".");
        private CountingMap countingMap     = new CountingMap();

        public static void main(String[] args, PrintStream err) {
            ClassDumpTool ps = new ClassDumpTool();
            ps.start(args, err);
            ps.stop();
        }

        public void run() {
            // walk through the system dictionary
            SystemDictionary dict = VM.getVM().getSystemDictionary();
            LogUtil.info("Output directory: " + outputDirectory);
            dict.classesDo(new SystemDictionary.ClassVisitor() {

                public void visit(Klass k) {
                    if (k instanceof InstanceKlass) {
                        try {
                            dumpKlass((InstanceKlass) k);
                        } catch (Exception e) {
                            LogUtil.error(k.getName().asString(), e);
                        }
                    }
                }
            });
        }

        private void dumpKlass(InstanceKlass kls) {
            String klassName = kls.getName().asString();
            String className = klassName.replace('/', '.');

            if ((pattern != null) && !pattern.matcher(className).matches()) {
                return;
            }
            LogUtil.info("Dump class: " + className + "  @ " + getClassLoaderName(kls));
            if (!UnixArgsutil.ARGS.flags().contains("hidejar")) {
                LogUtil.info("       jar: " + getClassJarFileName(kls));
            }

            klassName = klassName.replace('/', File.separatorChar);
            int index = klassName.lastIndexOf(File.separatorChar);
            File dir = null;
            if (index != -1) {
                String dirName = klassName.substring(0, index);
                dir = new File(outputDirectory, dirName);
            } else {
                dir = new File(outputDirectory);
            }
            long countIdx = countingMap.getAndCount(className, 1L);
            if (countIdx > 0) {
                LogUtil.warn("Have duplicate class in your JVM: " + className);
            }

            dir.mkdirs();
            File f = new File(dir, klassName.substring(klassName.lastIndexOf(File.separatorChar) + 1) + ".class"
                                   + ((countIdx == 0) ? "" : "_" + countIdx));
            try {
                f.createNewFile();
                OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
                try {
                    ClassWriter cw = new ClassWriter(kls, os);
                    cw.write();
                } finally {
                    os.close();
                }
            } catch (IOException exp) {
                LogUtil.error(null, exp);
            }
        }
    }

    public static String getClassJarFileName(InstanceKlass kls) {
        Oop protectionDomain = kls.getProtectionDomain();
        if (protectionDomain == null) {
            return null;
        }
        Oop codesource = getOopFieldValueFrom(protectionDomain, "codesource", "Ljava/security/CodeSource;");
        if (codesource == null) {
            return null;
        }
        Oop location = getOopFieldValueFrom(codesource, "location", "Ljava/net/URL;");
        if (location == null) {
            return null;
        }
        Oop path = getOopFieldValueFrom(location, "path", "Ljava/lang/String;");
        if (path == null) {
            return null;
        }
        return OopUtilities.stringOopToString(path);
    }

    public static String getClassLoaderName(InstanceKlass kls) {
        if ((kls.getClassLoader() != null) && (kls.getClassLoader() instanceof Instance)) {
            Instance cli = (Instance) kls.getClassLoader();
            return cli.getKlass().getName().asString().replace('/', '.');
        }
        return null; // should be bootstrap classloader
    }

    private static Oop getOopFieldValueFrom(Oop oop, String name, String sig) {
        InstanceKlass klass = (InstanceKlass) oop.getKlass();
        OopField field = (OopField) klass.findField(name, sig);
        return field.getValue(oop);
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar classdumpall.jar [options] <PID>");
        System.out.println("    -filter <class name regex>       filter by classname");
        System.out.println("    -output <dir>                    output directory");
        System.out.println("    --i                              ignore case");
        System.out.println("    --hidejar                        hide from jar");
        System.out.println();
        HotSpotProcessUtil.printVMs(System.out, true);
        System.exit(0);
    }
}
