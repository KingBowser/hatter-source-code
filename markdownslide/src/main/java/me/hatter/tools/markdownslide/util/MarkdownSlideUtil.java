package me.hatter.tools.markdownslide.util;

import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.string.StringUtil;

public class MarkdownSlideUtil {

    public static String processMds(String mds) {
        if (mds == null) {
            return null;
        }
        List<String> lines = IOUtil.readToList(mds);
        List<String> result = new ArrayList<String>(lines.size());
        for (String line : lines) {
            if (line.matches("^\\-{3,}$")) {
                result.add("---");
            } else {
                result.add(line);
            }
        }
        return StringUtil.join(result, Environment.LINE_SEPARATOR);
    }
}
