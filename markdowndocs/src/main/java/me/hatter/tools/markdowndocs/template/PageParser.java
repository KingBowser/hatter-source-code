package me.hatter.tools.markdowndocs.template;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.hatter.tools.commons.args.UnixArgsUtil;
import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.log.LogTool;
import me.hatter.tools.commons.log.LogTools;
import me.hatter.tools.commons.resource.impl.URLResource;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.markdowndocs.TestMain;
import me.hatter.tools.markdowndocs.config.Config;
import me.hatter.tools.markdowndocs.config.GlobalVars;
import me.hatter.tools.markdowndocs.model.Image;
import me.hatter.tools.markdowndocs.model.Page;
import me.hatter.tools.markdowndocs.model.Section;
import me.hatter.tools.markdowndocs.model.SubSection;
import me.hatter.tools.resourceproxy.jsspexec.JsspExecutor;
import me.hatter.tools.resourceproxy.jsspexec.util.BufferWriter;
import net.coobird.thumbnailator.Thumbnails;

public class PageParser {

    private static final LogTool log = LogTools.getLogTool(PageParser.class);

    // public static void main(String[] args) {
    // System.out.println(JSON.toJSONString(parsePage(new File(
    // "/Users/hatterjiang/Code/hatter-source-code/markdowndocs/samplex/01-Top10"))));
    // }

    public static Page parsePage(Config config, String dirName) {
        if (StringUtil.isEmpty(dirName)) {
            return parsePage(config, null, GlobalVars.getBasePath());
        } else {
            return parsePage(config, dirName, new File(GlobalVars.getBasePath(), dirName));
        }
    }

    public static Page parsePage(Config config, String dirName, File dir) {
        if ((dir == null) || (!dir.exists())) {
            return null;
        }

        Page page = new Page();
        page.setPath(((StringUtil.isEmpty(dirName)) ? "/" : "/" + dirName + "/"));
        page.setHeaderCode(FileUtil.readFileToStringIfExists(new File(dir, "header.code")));
        page.setIndexCode(FileUtil.readFileToStringIfExists(new File(dir, "index.code")));
        page.setFooterCode(FileUtil.readFileToStringIfExists(new File(dir, "footer.code")));
        page.setSummary(Markdown4jParser.parseMarkdown(new File(dir, "summary.md")));
        page.setNotice(Markdown4jParser.parseMarkdown(new File(dir, "notice.md")));
        page.setIndex(Markdown4jParser.parseMarkdown(new File(dir, "index.md")));
        page.setFooter(Markdown4jParser.parseMarkdown(new File(dir, "footer.md")));

        if (StringUtil.isNotEmpty(dirName) && (page.getHeaderCode() == null)) {
            page.setHeaderCode(FileUtil.readFileToStringIfExists(new File(GlobalVars.getBasePath(), "header.code")));
        }
        if (StringUtil.isNotEmpty(dirName) && (page.getFooterCode() == null)) {
            page.setFooterCode(FileUtil.readFileToStringIfExists(new File(GlobalVars.getBasePath(), "footer.code")));
        }
        if (StringUtil.isNotEmpty(dirName) && (page.getFooter() == null)) {
            page.setFooter(Markdown4jParser.parseMarkdown(new File(GlobalVars.getBasePath(), "footer.md")));
        }

        File images = new File(dir, "images");
        if (images.exists() && images.isDirectory()) {
            parseImages(config, page, images);
        } else {
            File[] mds = listMdFiles(dir);
            if ((mds != null) && (mds.length > 0)) {
                sortMdFiles(mds);
                parseMdFiles(page, mds);
            }
        }
        return page;
    }

    private static void parseImages(final Config config, Page page, File images) {
        final List<Image> imageList = new ArrayList<Image>();
        images.listFiles(new FileFilter() {

            public boolean accept(File file) {
                boolean isHolder = false;
                if (file.getName().endsWith(".holder")) { // if holder
                    isHolder = true;
                    log.info("Holder found: " + file);
                    file = new File(file.getParentFile(), StringUtil.substringBeforeLast(file.getName(), "."));
                }

                String fullName = file.getName();
                String name = StringUtil.substringBeforeLast(fullName, ".");
                String type = StringUtil.lower(StringUtil.substringAfterLast(fullName, "."));

                if (!Arrays.asList("jpg", "jpeg", "png", "bmp").contains(type)) {
                    return false;
                }
                if (fullName.matches(".*_\\d+x\\d+\\.\\w+$")) {
                    log.info("Skip file: " + file);
                    return false;
                }

                final Image img = new Image();
                imageList.add(img);
                img.setOriName(fullName);

                img.setSmallName(generateThumbnail(config, img, file, name, config.getSmallSize().intValue(), isHolder));
                img.setBigName(generateThumbnail(config, img, file, name, config.getBigSize().intValue(), isHolder));

                if (UnixArgsUtil.ARGS.flags().contains("clearimages") && (!isHolder)) {
                    log.info("Clear image: " + file);
                    FileUtil.writeStringToFile(new File(file.getParentFile(), file.getName() + ".holder"),
                                               "IMAGE_HOLDER");
                    file.delete();
                }

                return false;
            }

            private String generateThumbnail(final Config config, Image img, File file, String name, int size,
                                             boolean isHolder) {
                final String suffix = "_" + size + "x" + size;
                File imageFile = new File(file.getParentFile(), name + suffix + "." + config.getImgType());
                if (imageFile.exists()) {
                    log.info("Thumbnail exists: " + imageFile);
                } else {
                    if (isHolder) {
                        log.error("Cannot generate thumbnail from holder: " + file);
                    } else {
                        log.info("Generate thumbnail: " + imageFile);
                        try {
                            Thumbnails.of(file).size(size, size).outputQuality(config.getQuality()).toFile(imageFile);
                        } catch (IOException e) {
                            log.error("Generate thumbnail failed: " + file, e);
                        }
                    }
                }
                return imageFile.getName();
            }
        });

        Map<String, Object> addContext = new HashMap<String, Object>();
        addContext.put("images", imageList);

        BufferWriter bw = new BufferWriter();
        URLResource resource = new URLResource(
                                               TestMain.class.getResource("/"
                                                                          + ParameterParser.getGlobalParamter().getTemplate()
                                                                          + "/templates/images.template.jssp"),
                                               "images.template.jssp");
        JsspExecutor.executeJssp(resource, new HashMap<String, Object>(), addContext, null, bw);
        String index = page.getIndex();
        if (StringUtil.isBlank(index)) {
            page.setIndex(bw.getBufferedString());
        } else {
            page.setIndex(index.replace("$IMAGES$", bw.getBufferedString()));
        }
    }

    private static void parseMdFiles(Page page, File[] mds) {
        Map<File, List<File>> mdMap = new LinkedHashMap<File, List<File>>();
        File lastSMd = null;
        for (File md : mds) {
            if (lastSMd == null) {
                // first section
                lastSMd = md;
                mdMap.put(lastSMd, new ArrayList<File>());
            } else {
                if (md.getName().startsWith(lastSMd.getName().substring(0, lastSMd.getName().length() - 3))) {
                    // subsection
                    mdMap.get(lastSMd).add(md);
                } else {
                    // new section
                    lastSMd = md;
                    mdMap.put(lastSMd, new ArrayList<File>());
                }
            }
        }

        page.setSections(new ArrayList<Section>());
        for (Entry<File, List<File>> mdEntry : mdMap.entrySet()) {
            Section section = new Section();

            File md = mdEntry.getKey();
            String id = md.getName().substring(0, md.getName().length() - 3);
            List<String> lines = new ArrayList<String>(IOUtil.readToList(FileUtil.readFileToString(md)));
            boolean pageExplain = StringUtil.equals("!!EXPLAIN", StringUtil.trim(CollectionUtil.firstObject(lines)));

            if (CollectionUtil.isEmpty(mdEntry.getValue()) && (pageExplain)) {
                parseExplainedSection(section, id, parseExplain(lines));
            } else {
                SubSection _secSection = parseSubSection(id, lines);
                section.setId(_secSection.getId());
                section.setName(_secSection.getName());
                section.setTitle(_secSection.getTitle());
                section.setContent(_secSection.getContent());
                section.setSubSections(new ArrayList<SubSection>());
                for (File smd : mdEntry.getValue()) {
                    section.getSubSections().add(parseSubSection(smd));
                }
            }
            page.getSections().add(section);
        }
    }

    private static void sortMdFiles(File[] mds) {
        Arrays.sort(mds, new Comparator<File>() {

            public int compare(File f1, File f2) {
                String n1 = f1.getName().substring(0, f1.getName().length() - 3);
                String n2 = f2.getName().substring(0, f2.getName().length() - 3);
                if (n1.startsWith(n2)) {
                    return 1;
                }
                if (n2.startsWith(n1)) {
                    return -1;
                }
                return n1.compareTo(n2);
            }
        });
    }

    private static File[] listMdFiles(File dir) {
        File[] mds;
        mds = dir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (!name.endsWith(".md")) {
                    return false;
                }

                if (Arrays.asList("summary.md", "notice.md", "index.md", "footer.md").contains(name)) {
                    return false;
                }

                return true;
            }
        });
        return mds;
    }

    public static void parseExplainedSection(Section section, String id, List<List<String>> groupList) {
        List<String> firstGroup = groupList.get(0);
        SubSection _secSection = parseSubSection(id, firstGroup);
        section.setId(_secSection.getId());
        section.setName(_secSection.getName());
        section.setTitle(_secSection.getTitle());
        section.setContent(_secSection.getContent());
        section.setSubSections(new ArrayList<SubSection>());
        if (groupList.size() > 1) {
            for (int i = 1; i < groupList.size(); i++) {
                List<String> group = groupList.get(i);
                String firstLine = CollectionUtil.firstObject(group);
                String _firstLine = firstLine.substring(3);
                String _id = String.valueOf(i);
                if (StringUtil.contains(_firstLine, "#")) {
                    _id = StringUtil.substringBefore(_firstLine, "#");
                    _firstLine = StringUtil.substringAfter(_firstLine, "#");
                }
                group.set(0, _firstLine);
                section.getSubSections().add(parseSubSection(id + "--" + _id, group));
            }
        }
    }

    public static List<List<String>> parseExplain(List<String> lines) {
        List<List<String>> groupList = new ArrayList<List<String>>();
        // !!#xxx# xxxxx
        List<String> group = new ArrayList<String>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("!!#")) {
                if (!group.isEmpty()) {
                    groupList.add(group);
                    group = new ArrayList<String>();
                }
            }
            group.add(line);
        }
        if (!group.isEmpty()) {
            groupList.add(group);
        }
        return groupList;
    }

    public static SubSection parseSubSection(File md) {
        String id = md.getName().substring(0, md.getName().length() - 3);
        List<String> lines = new ArrayList<String>(IOUtil.readToList(FileUtil.readFileToString(md)));
        return parseSubSection(id, lines);
    }

    public static SubSection parseSubSection(String id, List<String> lines) {

        // remove leading numbers
        Matcher m = Pattern.compile("\\d{1,4}\\-(.+)").matcher(id);
        if (m.matches()) {
            id = m.group(1);
        }

        SubSection subSection = new SubSection();
        subSection.setId(id);
        if (lines.size() > 0) {
            String firstLine = lines.remove(0);
            String name = firstLine;
            String title = firstLine;
            if (firstLine.contains(":::")) {
                name = StringUtil.substringBefore(firstLine, ":::");
                title = StringUtil.substringAfter(firstLine, ":::");
            }
            subSection.setName(name);
            subSection.setTitle(title);
            // subSection.setName(Markdown4jParser.parseMarkdown(name));
            // subSection.setTitle(Markdown4jParser.parseMarkdown(title));
        }
        subSection.setContent(Markdown4jParser.parseMarkdown(StringUtil.join(lines, "\n")));
        return subSection;
    }
}
