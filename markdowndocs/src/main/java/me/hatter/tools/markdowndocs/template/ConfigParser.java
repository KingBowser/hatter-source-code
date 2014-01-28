package me.hatter.tools.markdowndocs.template;

import java.io.File;

import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdowndocs.config.Config;
import me.hatter.tools.markdowndocs.config.Configs;
import me.hatter.tools.markdowndocs.config.GlobalVars;

import com.alibaba.fastjson.JSONObject;

public class ConfigParser {

    private static Config globalConfig;

    synchronized public static Config getGlobalConfig() {
        if (globalConfig != null) {
            return globalConfig;
        }
        File config = new File(GlobalVars.getBasePath(), "config.json");
        globalConfig = parseConfig(config);
        return globalConfig;
    }

    public static Config readConfig(String dirName) {
        if (StringUtil.isEmpty(dirName)) {
            return Configs.mergeConfig(getGlobalConfig(), Configs.DEFAULT);
        }
        File config = new File(new File(GlobalVars.getBasePath(), dirName), "config.json");
        Config conf = parseConfig(config);
        return Configs.mergeConfig(conf, getGlobalConfig(), Configs.DEFAULT);
    }

    private static Config parseConfig(File config) {
        if ((config != null) && (config.exists())) {
            return JSONObject.parseObject(FileUtil.readFileToString(config), Config.class);
        } else {
            return new Config();
        }
    }
}
