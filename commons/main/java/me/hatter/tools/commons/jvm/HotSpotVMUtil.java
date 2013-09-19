package me.hatter.tools.commons.jvm;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.environment.Environment.JavaVendor;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.os.OSUtil;
import me.hatter.tools.commons.os.OSUtil.OS;

public class HotSpotVMUtil {

    private static final LogTool logTool = LogTools.getLogTool(HotSpotVMUtil.class);

    public static enum JDKLib {

        TOOLS("tools.jar", //
              Arrays.asList(JavaVendor.Sun, JavaVendor.Oracle, JavaVendor.Unknow), //
              Arrays.asList(OS.Linux, OS.Unix, OS.MacOS)),

        // Use for load agent
        MANAGEMENT_AGENT("management-agent.jar", //
                         Arrays.asList(JavaVendor.Apple, JavaVendor.Sun, JavaVendor.Oracle, JavaVendor.Unknow), //
                         Arrays.asList(OS.Linux, OS.Unix, OS.MacOS, OS.Windows)),

        SA_JDI("sa-jdi.jar", //
               Arrays.asList(JavaVendor.Apple, JavaVendor.Sun, JavaVendor.Oracle, JavaVendor.Unknow), //
               Arrays.asList(OS.Linux, OS.Unix, OS.MacOS));

        private String          name;
        private Set<JavaVendor> supportedvVendors;
        private Set<OS>         supportedOS;

        public String getName() {
            return name;
        }

        public Set<JavaVendor> getSupportedJavaVendors() {
            return supportedvVendors;
        }

        public Set<OS> getSupportedOS() {
            return supportedOS;
        }

        private JDKLib(String name, List<JavaVendor> vendors, List<OS> oss) {
            this.name = name;
            this.supportedvVendors = Collections.unmodifiableSet(new HashSet<Environment.JavaVendor>(vendors));
            this.supportedOS = Collections.unmodifiableSet(new HashSet<OSUtil.OS>(oss));
        }
    }

    public static enum JDKTarget {
        SYSTEM_CLASSLOADER,

        @Deprecated
        // Actually, I don't know how to add bootstrap classpath at runtime
        // Now I know, but it a little difficult, use agent can do this
        BOOTSTRAP_CLASSPATH;
    }

    private static Set<JDKLib> addedSysJDKLibs  = new HashSet<HotSpotVMUtil.JDKLib>();
    private static Set<JDKLib> addedBootJDKLibs = new HashSet<HotSpotVMUtil.JDKLib>();

    @SuppressWarnings("restriction")
    synchronized public static void autoAddToolsJarDependency(JDKTarget target, JDKLib jdkLib) {
        Set<JDKLib> addedJDKLibs = (target == JDKTarget.SYSTEM_CLASSLOADER) ? addedSysJDKLibs : addedBootJDKLibs;
        if ((!addedJDKLibs.contains(jdkLib)) && jdkLib.getSupportedJavaVendors().contains(Environment.getVendor())
            && jdkLib.getSupportedOS().contains(OSUtil.getOS())) {
            File toolsJar = new File(Environment.JDK_HOME, "lib/" + jdkLib.getName()).getAbsoluteFile();
            if (!toolsJar.exists()) {
                logTool.error("JDK " + jdkLib.getName() + " not found: " + toolsJar.getPath());
                return;
            }
            try {
                logTool.info("Add " + ((target == JDKTarget.SYSTEM_CLASSLOADER) ? "system" : "bootstrp")
                             + " classloader jar url: " + toolsJar);
                if (target == JDKTarget.SYSTEM_CLASSLOADER) {
                    ClassLoaderUtil.addURLs(ClassLoaderUtil.getSystemClassLoader(), toolsJar.toURI().toURL());
                } else {
                    sun.misc.Launcher.getBootstrapClassPath().addURL(toolsJar.toURI().toURL());
                }
                addedJDKLibs.add(jdkLib);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
