package me.hatter.tools.markdowndocs.template;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdowndocs.config.GlobalVars;
import me.hatter.tools.markdowndocs.model.MenuItem;

public class MenuParser {

    public static void parseMenuItems(List<MenuItem> refLefts, List<MenuItem> refRights) {
        File[] dirs;
        dirs = GlobalVars.getBasePath().listFiles(new FileFilter() {

            public boolean accept(File file) {
                if (!file.isDirectory()) {
                    return false;
                }
                if (file.getName().startsWith(".")) {
                    return false;
                }
                if (Arrays.asList("assets").contains(file.getName())) {
                    return false;
                }
                return true;
            }
        });

        Arrays.sort(dirs, new Comparator<File>() {

            public int compare(File d1, File d2) {
                return d1.getName().compareTo(d2.getName());
            }
        });

        MenuItem main = new MenuItem();
        main.setPath("/");
        main.setDirName(null);
        main.setTitle(StringUtil.defaultValue(ConfigParser.readConfig(null).getName(), "Index"));
        refLefts.add(main);
        for (File dir : dirs) {
            List<MenuItem> items = (dir.getName().startsWith("_")) ? refRights : refLefts;
            String dirName = dir.getName();
            MenuItem item = new MenuItem();
            item.setPath("/" + dirName + "/");
            item.setDirName(dirName);
            item.setTitle(StringUtil.defaultValue(ConfigParser.readConfig(dirName).getName(), parseName(dirName)));
            items.add(item);
        }
    }

    public static String parseName(String dirName) {
        if (dirName.startsWith("_")) {
            dirName = dirName.substring(1);
        }
        Matcher m = Pattern.compile("\\d{1,3}\\-(.*)").matcher(dirName);
        if (m.matches()) {
            dirName = m.group(1);
        }
        return dirName;
    }
}
