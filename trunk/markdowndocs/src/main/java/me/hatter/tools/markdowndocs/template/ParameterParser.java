package me.hatter.tools.markdowndocs.template;

import java.io.File;

import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.markdowndocs.config.GlobalVars;
import me.hatter.tools.markdowndocs.config.Parameter;

import com.alibaba.fastjson.JSONObject;

public class ParameterParser {

    private static Parameter parameter;

    synchronized public static Parameter getGlobalParamter() {
        if (parameter != null) {
            return parameter;
        }
        File md = new File(GlobalVars.getBasePath(), "markdowndocs.json");
        parameter = parseParameter(md);
        return parameter;
    }

    private static Parameter parseParameter(File md) {
        try {
            return JSONObject.parseObject(FileUtil.readFileToString(md), Parameter.class);
        } catch (Exception e) {
            throw new IllegalStateException("Parse parameter failed: " + md, e);
        }
    }
}
