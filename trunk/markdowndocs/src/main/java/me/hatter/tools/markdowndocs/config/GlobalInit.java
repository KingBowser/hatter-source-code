package me.hatter.tools.markdowndocs.config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.markdowndocs.assets.Asset;

public class GlobalInit {

    private static final LogTool log = LogTools.getLogTool(GlobalInit.class);

    public static void initAsset(Asset asset) throws IOException {
        log.info("Init asset: " + asset.getTemplate());
        for (String resource : asset.getResources()) {
            File file = new File(new File(GlobalVars.getBasePath(), "assets"), resource);
            file.getParentFile().mkdirs();
            if ((!file.exists()) || GlobalVars.isOverrideAssets()) {
                BufferedOutputStream bos = IOUtil.asBufferedOutputStream(new FileOutputStream(file));
                try {
                    log.info("Write asset file: " + file);
                    IOUtil.copy(GlobalInit.class.getResourceAsStream("/" + asset.getTemplate() + "/assets/" + resource),
                                bos);
                } finally {
                    IOUtil.closeQuietly(bos);
                }
            }
        }
    }
}
