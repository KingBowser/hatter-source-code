package me.hatter.tools.markdowndocs.template;

import java.io.File;
import java.io.IOException;

import me.hatter.tools.commons.file.FileUtil;

import org.markdown4j.Markdown4jProcessor;

public class Markdown4jParser {

    public static String parseMarkdown(File file) {
        if (!file.exists()) {
            return null;
        }
        return parseMarkdown(FileUtil.readFileToString(file));
    }

    public static String parseMarkdown(String text) {
        if (text == null) {
            return null;
        }
        try {
            return new Markdown4jProcessor().process(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
