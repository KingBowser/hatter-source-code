package me.hatter.tools.resourceproxy.dbutils.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.string.StringUtil;

public class GenerateJavaFields {

    private static LogTool logTool = LogTools.getLogTool(GenerateJavaFields.class);
    static final String    REGEX   = StringUtil.trim(IOUtil.readResourceToString(GenerateJavaFields.class, "regex.txt"));
    static final Pattern   PATTERN = Pattern.compile(REGEX);

    public static void main(String[] args) {
        // System.out.println(REGEX);
        System.out.println();
        String desc = FileUtil.readFileToString(new File(Environment.getUserDesktop(), "desc.txt"));
        List<String> descLines = IOUtil.readToList(desc);
        for (String line : descLines) {
            if (line.contains("----") || StringUtil.isBlank(line)) {
                continue;
            }
            Matcher m = PATTERN.matcher(line);
            if (!m.matches()) {
                logTool.warn("Not Match: " + line);
            } else {
                String field = m.group(1).trim();
                String type = m.group(2).trim();
                // String nulls = m.group(3).trim();
                String key = m.group(4).trim();
                // String defaults = m.group(5).trim();
                // String extra = m.group(6).trim();

                if (field.equals("Field")) {
                    continue;
                }

                String _field = StringUtil.toCamel(field);
                String _type = StringUtil.toLowerCase((type.contains("(")) ? StringUtil.substringBefore(type, "(") : type);
                // System.out.println(Arrays.asList(StringUtil.toCamel(field), field, type, nulls, key, defaults,
                // extra));
                List<String> result = new ArrayList<String>();
                result.add("private");
                if (Arrays.asList("int", "tinyint", "bigint", "smallint").contains(_type)) {
                    result.add("Integer");
                } else if (Arrays.asList("varchar", "char", "enum").contains(_type)) {
                    result.add("String");
                } else if (Arrays.asList("datetime").contains(_type)) {
                    result.add("Date");
                } else {
                    logTool.error("Unknow type: " + line);
                }
                result.add(_field);
                result.add(";");
                if (key.equals("PRI")) {
                    System.out.println("@Field(pk=true)");
                }
                System.out.println(StringUtil.join(result, " "));
            }
        }
    }
}
