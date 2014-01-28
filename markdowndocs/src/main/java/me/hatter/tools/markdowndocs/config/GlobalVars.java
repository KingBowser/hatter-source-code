package me.hatter.tools.markdowndocs.config;

import java.io.File;

import me.hatter.tools.commons.environment.Environment;

public class GlobalVars {

    private static File    basePath       = new File(Environment.USER_DIR);
    private static boolean overrideAssets = false;

    public static File getBasePath() {
        return basePath;
    }

    public static void setBasePath(File basePath) {
        GlobalVars.basePath = basePath;
    }

    public static boolean isOverrideAssets() {
        return overrideAssets;
    }

    public static void setOverrideAssets(boolean overrideAssets) {
        GlobalVars.overrideAssets = overrideAssets;
    }
}
