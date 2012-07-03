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
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.os.OSUtil;
import me.hatter.tools.commons.os.OSUtil.OS;

public class HotSpotVMUtil {

    public static enum JDKLib {

        TOOLS("tools.jar", Arrays.asList(OS.Linux, OS.Unix)),

        SA_JDI("sa-jdi.jar", Arrays.asList(OS.Linux, OS.Unix, OS.MacOS));

        private String  name;
        private Set<OS> supporedOS;

        public String getName() {
            return name;
        }

        public Set<OS> getSupportedOS() {
            return supporedOS;
        }

        private JDKLib(String name, List<OS> oss) {
            this.name = name;
            this.supporedOS = Collections.unmodifiableSet(new HashSet<OSUtil.OS>(oss));
        }
    }

    public static enum JDKTarget {
        SYSTEM_CLASSLOADER, BOOTSTRAP_CLASSPATH;
    }

    private static Set<JDKLib> addedSysJDKLibs  = new HashSet<HotSpotVMUtil.JDKLib>();
    private static Set<JDKLib> addedBootJDKLibs = new HashSet<HotSpotVMUtil.JDKLib>();

    @SuppressWarnings("restriction")
    synchronized public static void autoAddToolsJarDependency(JDKTarget target, JDKLib jdkLib) {
        Set<JDKLib> addedJDKLibs = (target == JDKTarget.SYSTEM_CLASSLOADER) ? addedSysJDKLibs : addedBootJDKLibs;
        if ((!addedJDKLibs.contains(jdkLib)) && jdkLib.getSupportedOS().contains(OSUtil.getOS())) {
            File toolsJar = new File(Environment.JDK_HOME, "lib/" + jdkLib.getName()).getAbsoluteFile();
            if (!toolsJar.exists()) {
                LogUtil.error("JDK " + jdkLib.getName() + " not found: " + toolsJar.getPath());
                return;
            }
            try {
                LogUtil.info("Add " + ((target == JDKTarget.SYSTEM_CLASSLOADER) ? "system" : "bootstrp")
                             + " classloader jar url: " + toolsJar);
                if (target == JDKTarget.SYSTEM_CLASSLOADER) {
                    ClassLoaderUtil.addURLs(ClassLoaderUtil.getSystemURLClassLoader(), toolsJar.toURI().toURL());
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
