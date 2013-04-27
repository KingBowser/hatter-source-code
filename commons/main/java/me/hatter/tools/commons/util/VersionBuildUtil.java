package me.hatter.tools.commons.util;

import java.io.IOException;
import java.net.URL;

import me.hatter.tools.commons.classloader.ClassLoaderUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.string.StringUtil;

public class VersionBuildUtil {

    public static String getVersionBuild() {
        URL u = ClassLoaderUtil.getAppClassLoader().findResource("commons-version-build.txt");
        if (u == null) {
            return null;
        }
        try {
            return StringUtil.trim(IOUtil.readToString(u.openStream()));
        } catch (IOException e) {
            return "$error_version_build";
        }
    }
}
