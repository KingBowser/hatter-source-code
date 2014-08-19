package me.hatter.tools.markdownslide.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.io.IOUtil;

import com.alibaba.fastjson.JSON;

public class Configs {

    private static Config config;

    synchronized public static Config getConfig() {
        if (config == null) {
            File f = new File(Environment.USER_DIR, "markdownslide.json");
            if (!f.exists()) {
                throw new RuntimeException("File `markdownslide.json` not found!");
            }
            try {
                config = JSON.parseObject(IOUtil.readToStringAndClose(new FileInputStream(f)), Config.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return config;
    }
}
