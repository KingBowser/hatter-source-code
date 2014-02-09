package me.hatter.tools.markdowndocs;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.args.UnixArgsUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdowndocs.plugin.ConvertPlugin;
import me.hatter.tools.markdowndocs.plugin.ThumbnailatorPlugin;

public class Plugins {

    private static final LogTool log     = LogTools.getLogTool(Plugins.class);

    private static List<Plugin>  plugins = new ArrayList<Plugin>();
    static {
        plugins.add(new ConvertPlugin());
        plugins.add(new ThumbnailatorPlugin());

        for (Plugin plugin : plugins) {
            plugin.init();
        }
    }

    public static void runPlugin(final String plg) {
        String _plg = StringUtil.lower(StringUtil.trim(plg));
        for (Plugin plugin : plugins) {
            if (plugin.matches(_plg)) {
                plugin.main(UnixArgsUtil.ARGS);
                return;
            }
        }
        log.warn("Cannot find any plugin for: " + plg);
    }
}
