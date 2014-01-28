package me.hatter.tools.markdowndocs.config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.markdowndocs.assets.Assets;

public class GlobalInit {

    private static final LogTool log = LogTools.getLogTool(GlobalInit.class);

    public static void initAssets() throws IOException {
        initAssetsDir("css", Assets.CSSES);
        initAssetsDir("js", Assets.JSES);
        initAssetsDir("img", Assets.IMGS);
    }

    public static void initAssetsDir(String dirName, List<String> assets) throws IOException {
        log.info("Init assets: " + dirName);
        File dir = new File(GlobalVars.getBasePath(), dirName);
        dir.mkdirs();
        for (String asset : assets) {
            File file = new File(dir, asset);
            if ((!file.exists()) || GlobalVars.isOverrideAssets()) {
                BufferedOutputStream bos = IOUtil.asBufferedOutputStream(new FileOutputStream(file));
                try {
                    log.info("Write asset file: " + file);
                    IOUtil.copy(GlobalInit.class.getResourceAsStream("/assets/" + dirName + "/" + asset), bos);
                } finally {
                    IOUtil.closeQuietly(bos);
                }
            }
        }
    }
}
