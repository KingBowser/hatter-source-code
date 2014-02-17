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

public class ConvertPlugin implements Plugin {

    private static final LogTool log = LogTools.getLogTool(ConvertPlugin.class);

    public void init() {
    }

    public boolean matches(String plg) {
        return Arrays.asList("con", "conv", "convert").contains(plg);
    }

    public void printHelp() {
        // XXX
    }

    public void main(KArgs args) {
        final List<String> _t = CollectionUtil.toLowerCase(args.kvalues("type"));
        final String _ext = StringUtil.lower(args.kvalueAny("ext"));
        final String _quality = args.kvalueAny("quality");
        if (CollectionUtil.isEmpty(_t) || StringUtil.isEmpty(_ext)) {
            log.error("Args not complete: type, ext, quality");
            return;
        }
        final double quality = Double.parseDouble(_quality);
        GlobalVars.getBasePath().listFiles(new FileFilter() {

            public boolean accept(File file) {
                String name = StringUtil.substringBeforeLast(file.getName(), ".");
                String type = StringUtil.lower(StringUtil.substringAfterLast(file.getName(), "."));

                if (!_t.contains(type)) {
                    return false;
                }

                if (_ext.equals(type)) {
                    log.info("Skip file: " + file);
                    return false;
                }

                File newImage = new File(GlobalVars.getBasePath(), name + "." + _ext);
                log.info("Convert file: " + newImage);
                try {
                    Thumbnails.of(file).scale(1.0D).outputQuality(quality).toFile(newImage);
                } catch (IOException e) {
                    log.error("Convert file faile: " + file, e);
                }
                return true;
            }
        });
    }
}
