package me.hatter.tools.commons.jvm;

import java.io.File;
import java.net.MalformedURLException;

import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.os.OSUtil;

public class HotSpotVMUtil {

    private static boolean isAddedToolsJarDependency = false;

    synchronized public static void autoAddToolsJarDependency() {
        if ((!isAddedToolsJarDependency) && OSUtil.isUnixCompatible() && (!OSUtil.isMacOS())) {
            String javaHome = Environment.JAVA_HOME.replaceAll("\\/jre(\\/)?$", "");
            File toolsJar = new File(javaHome, "lib/tools.jar").getAbsoluteFile();
            if (!toolsJar.exists()) {
                LogUtil.error("JDK tools.jar not found: " + toolsJar.getPath());
                return;
            }
            try {
                LogUtil.info("Add system classloader jar url: " + toolsJar);
                ClassLoaderUtil.addURLs(ClassLoaderUtil.getSystemURLClassLoader(), toolsJar.toURI().toURL());
                isAddedToolsJarDependency = true;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
