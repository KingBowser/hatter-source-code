package me.hatter.tools.markdowndocs.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import me.hatter.tools.commons.args.KArgs;
import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdowndocs.Plugin;
import me.hatter.tools.markdowndocs.config.GlobalVars;
import net.coobird.thumbnailator.Thumbnails;

public class ThumbnailatorPlugin implements Plugin {

    private static final LogTool log = LogTools.getLogTool(ThumbnailatorPlugin.class);

    public void init() {
    }

    public boolean matches(String plg) {
        return Arrays.asList("slt", "thumb", "thumbnail", "tn").contains(plg);
    }

    public void main(KArgs args) {
        final List<String> _t = CollectionUtil.toLowerCase(args.kvalues("type"));
        final String _w = args.kvalueAny("width");
        final String _h = args.kvalueAny("height");
        final String _ext = args.kvalueAny("ext");
        final String _quality = args.kvalueAny("quality");
        if (CollectionUtil.isEmpty(_t) || StringUtil.isEmpty(_w) || StringUtil.isEmpty(_h)) {
            log.error("Args not complete: type, width, height");
            return;
        }
        final int w = Integer.parseInt(_w);
        final int h = Integer.parseInt(_h);
        final String suffix = "_" + w + "x" + h;
        final double quality = Double.parseDouble(StringUtil.defaultValue(_quality, "0.95"));

        GlobalVars.getBasePath().listFiles(new FileFilter() {

            public boolean accept(File file) {
                String name = StringUtil.substringBeforeLast(file.getName(), ".");
                String type = StringUtil.lower(StringUtil.substringAfterLast(file.getName(), "."));

                if (!_t.contains(type)) {
                    return false;
                }

                if (name.endsWith(suffix)) {
                    log.info("Skip file: " + file);
                    return false;
                }

                File newImage = new File(GlobalVars.getBasePath(), name + suffix + "."
                                                                   + (StringUtil.isEmpty(_ext) ? type : _ext));
                log.info("Generate thumbnail: " + newImage);
                try {
                    Thumbnails.of(file).size(w, h).outputQuality(quality).toFile(newImage);
                } catch (IOException e) {
                    log.error("Generate thumbnail failed: " + file, e);
                }
                return true;
            }
        });
    }
}