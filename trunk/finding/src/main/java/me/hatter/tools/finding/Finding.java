package me.hatter.tools.finding;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.concurrent.ExecutorUtil;
import me.hatter.tools.commons.encoding.EncodingDetectUtil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.IOUtil;
import me.hatter.tools.commons.io.StringBufferedReader;
import me.hatter.tools.commons.io.StringPrintWriter;
import me.hatter.tools.commons.io.SysOutUtil;
import me.hatter.tools.commons.log.LogUtil;
import me.hatter.tools.commons.number.IntegerUtil;
import me.hatter.tools.commons.resource.Resource;
import me.hatter.tools.commons.resource.impl.FileResource;
import me.hatter.tools.commons.resource.impl.ZipEntryResource;
import me.hatter.tools.commons.string.StringUtil;
import me.hatter.tools.commons.xml.XmlParser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Finding {

    public static final char          CHAR_27    = (char) 27;
    public static final String        RESET      = CHAR_27 + "[0m";
    private static final Object       sysOutLock = new Object();

    private static List<MatchPattern> ffList     = new ArrayList<MatchPattern>();

    public static class MatchPattern {

        public boolean      isInclude;
        public RegexMatcher matcher;

        public MatchPattern(boolean isInclude, RegexMatcher matcher) {
            this.isInclude = isInclude;
            this.matcher = matcher;
        }
    }

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);
        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }

        final String o = UnixArgsutil.ARGS.kvalue("o");
        if ((o != null) && (!o.isEmpty())) {
            try {
                "test".getBytes(o);
                SysOutUtil.setOutputCharset(o);
            } catch (UnsupportedEncodingException e) {
                LogUtil.warn("Charset is not supported: " + o);
            }
        }

        final Set<String> extSet = getExtSet();
        final String search = UnixArgsutil.ARGS.args()[0];
        final Matcher matcher = getMatcher(search);

        List<String> ffs = UnixArgsutil.ARGS.kvalues("ff");
        if ((ffs != null) && (!ffs.isEmpty())) {
            for (String ff : ffs) {
                if ((ff != null) && (!ff.isEmpty())) {
                    if (ff.startsWith("~")) {
                        ffList.add(new MatchPattern(false, new RegexMatcher(null, ff.substring(1), true)));
                    } else {
                        ffList.add(new MatchPattern(true, new RegexMatcher(null, ff, true)));
                    }
                }
            }
        }

        final boolean is_0 = UnixArgsutil.ARGS.flags().contains("0");
        final boolean is_1 = UnixArgsutil.ARGS.flags().contains("1");
        final boolean is_s = UnixArgsutil.ARGS.flags().contains("s");
        final boolean is_F = UnixArgsutil.ARGS.flags().contains("F");
        final boolean is_N = UnixArgsutil.ARGS.flags().contains("N");
        final boolean is_C = UnixArgsutil.ARGS.flags().contains("C");
        final boolean is_L = UnixArgsutil.ARGS.flags().contains("L");
        final long startMillis = System.currentTimeMillis();
        final AtomicLong totalCount = new AtomicLong(0);
        final AtomicLong fileCount = new AtomicLong(0);
        final AtomicLong matchCount = new AtomicLong(0);

        final ExecutorService executor = ExecutorUtil.getCPULikeExecutor(IntegerUtil.tryParse(UnixArgsutil.ARGS.kvalue("CC")));

        final MatchResourceFilter matchResourceFilter = new MatchResourceFilter() {

            public void matchResource(Resource resource) {

                if (resource instanceof FileResource) {
                    if (((FileResource) resource).getFile().isDirectory()) {
                        return; // only match file
                    }
                }
                boolean isPackageResource = false;
                if (extSet != null) {
                    String ext = StringUtil.substringAfterLast(resource.getResourceId(), ".");
                    if (ext == null) {
                        return;
                    }
                    if (!extSet.contains(ext.toLowerCase())) {
                        return;
                    }
                    isPackageResource = Arrays.asList("jar", "war", "sar", "zip").contains(ext.toLowerCase());
                }
                if (!isPathMattch(resource)) {
                    return;
                }
                if (isPackageResource && (resource instanceof ZipEntryResource)) {
                    return; // SKIP JAR IN JAR
                }
                if (isPackageResource) {
                    ZipFile zipFile;
                    try {
                        zipFile = new ZipFile(((FileResource) resource).getFile());
                        Enumeration<? extends ZipEntry> entries = zipFile.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry zipEntry = entries.nextElement();
                            if (!zipEntry.getName().endsWith("/")) {
                                matchResource(new ZipEntryResource(zipFile, zipEntry));
                            }
                        }
                    } catch (Exception e) {
                        LogUtil.error("Read zip file failed: " + ((FileResource) resource).getFile().toString(), e);
                    }
                    return; // SKIP NEXT
                }

                fileCount.incrementAndGet();
                int mcount = 0;
                int linenumber = 0;
                String text = EncodingDetectUtil.detectString(IOUtil.readToBytesAndClose(resource.openInputStream()),
                                                              null, "UTF-8", "GB18030"); // auto detect encoding
                StringBufferedReader reader = new StringBufferedReader(text);
                for (String line; ((line = reader.readOneLine()) != null);) {
                    String ln = line.trim();
                    totalCount.incrementAndGet();
                    boolean is_match = matcher.match(ln);
                    if (is_match) {
                        String fileColorSt = is_C ? (CHAR_27 + "[;102m") : "";
                        String matchColorSt = is_C ? (CHAR_27 + "[;101m") : "";
                        String colorEd = is_C ? RESET : "";
                        String fn;
                        if (is_F) {
                            fn = resource.getResourceId();
                        } else if (is_s) {
                            fn = resource.getResourceName();
                        } else {
                            fn = resource.getResourceId();
                            if (fn.replace("file:" + Environment.USER_DIR, StringUtil.EMPTY).length() != fn.length()) {
                                fn = "." + fn.replace("file:" + Environment.USER_DIR, StringUtil.EMPTY);
                            }
                            if (fn.startsWith("file:")) {
                                fn = fn.substring(5);
                            }
                            fn = (fn.startsWith("././")) ? fn.substring(2) : fn;
                        }
                        fn = fileColorSt + fn + colorEd;

                        String outln = ln;
                        if (is_C && (matcher instanceof ContainsMatcher)) {
                            outln = StringUtil.EMPTY;
                            boolean _is_i = UnixArgsutil.ARGS.flags().contains("i");
                            String mln = _is_i ? ln.toLowerCase() : ln;
                            String oln = ln;
                            String mse = _is_i ? search.toLowerCase() : search;
                            int matchIndex;
                            while ((matchIndex = mln.indexOf(mse)) >= 0) {
                                outln += oln.substring(0, matchIndex);
                                outln += matchColorSt;
                                outln += oln.substring(matchIndex, (matchIndex + mse.length()));
                                outln += colorEd;
                                mln = mln.substring(matchIndex + mse.length());
                                oln = oln.substring(matchIndex + mse.length());
                            }
                            outln += oln;
                        }

                        StringPrintWriter printWriter = new StringPrintWriter();
                        String _linenum = is_L ? "(" + StringUtil.paddingSpaceLeft(String.valueOf(linenumber), 5) + ")" : StringUtil.EMPTY;
                        if (is_N) {
                            if (mcount == 0) {
                                printWriter.println(fn);
                            }
                            if (!is_0) {
                                printWriter.println("\t" + _linenum + ": " + outln);
                            }
                        } else {
                            printWriter.print(fn);
                            if (!is_0) {
                                printWriter.print(_linenum + ": " + outln);
                            }
                            printWriter.println();
                        }
                        synchronized (sysOutLock) {
                            SysOutUtil.stdout.print(printWriter.toString());
                        }

                        if (is_0 || is_1) {
                            return;
                        }
                        if (mcount == 0) {
                            matchCount.incrementAndGet();
                        }
                        mcount++;
                    }
                    linenumber++;
                }
            }

            public boolean accept(final Resource resource) {

                if (resource instanceof FileResource) {
                    if (((FileResource) resource).getFile().isDirectory()) {
                        return (!((FileResource) resource).getFile().toString().contains(".svn"));
                    }
                }
                executor.submit(new Runnable() {

                    public void run() {
                        matchResource(resource);
                    }
                });
                return false;
            }
        };

        final File dir = new File(Environment.USER_DIR);
        File inf = null;
        if (UnixArgsutil.ARGS.kvalue("I") != null) {
            inf = new File(resolveUserPath(UnixArgsutil.ARGS.kvalue("I")));
        }

        if ((inf != null) && (!inf.exists())) {
            LogUtil.error("File or Directory not found: " + inf.toString());
            System.exit(-1);
        }

        if ((inf != null) && inf.isFile()) {
            String files = readFileToStringCheckClasspathFile(inf);

            StringBufferedReader reader = new StringBufferedReader(files);
            for (String file; ((file = reader.readOneLine()) != null);) {
                final File f_file = new File(file);
                if (!f_file.exists()) continue;
                if (f_file.isFile()) {
                    executor.submit(new Runnable() {

                        public void run() {
                            matchResourceFilter.matchResource(new FileResource(f_file));
                        }
                    });
                } else {
                    FileUtil.listFiles(f_file, new FileFilter() {

                        public boolean accept(File pathname) {
                            return matchResourceFilter.accept(new FileResource(pathname));
                        }
                    }, null);
                }
            }
        } else {
            FileUtil.listFiles(((inf == null) ? dir : inf), new FileFilter() {

                public boolean accept(File pathname) {
                    return matchResourceFilter.accept(new FileResource(pathname));
                }
            }, null);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LogUtil.error("Waite all thread(s) finish failed: ", e);
        }

        final long endMillis = System.currentTimeMillis();
        DecimalFormat format = new DecimalFormat("#,###,###");
        synchronized (sysOutLock) {
            SysOutUtil.stdout.println("Finish, Total: " + format.format(totalCount.get()) + ", Ext Match: "
                                      + format.format(fileCount.get()) + ", Txt Match: "
                                      + format.format(matchCount.get()) + ", Cost: "
                                      + format.format(endMillis - startMillis) + " ms");
        }
    }

    private static String readFileToStringCheckClasspathFile(File inf) {
        String content = FileUtil.readFileToString(inf);
        if (inf.toString().endsWith(".classpath") && content.contains("classpath")
            && content.contains("classpathentry")) {
            XmlParser xmlParser;
            try {
                xmlParser = new XmlParser(new FileInputStream(inf));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            File basePath = inf.getParentFile();
            Set<String> resultFileSet = new LinkedHashSet<String>();
            NodeList nodeList = xmlParser.parseXpathNodes("//classpathentry");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getAttributes().getNamedItem("kind") == null) continue;
                String kind = node.getAttributes().getNamedItem("kind").getNodeValue();
                if ("src".equals(kind)) {
                    if (node.getAttributes().getNamedItem("path") != null) {
                        resultFileSet.add(new File(basePath, node.getAttributes().getNamedItem("path").getNodeValue()).getAbsolutePath());
                    }
                } else if ("var".equals(kind)) {
                    if (node.getAttributes().getNamedItem("path") != null) {
                        resultFileSet.add(resolveVariantPath(node.getAttributes().getNamedItem("path").getNodeValue()));
                    }
                    if (node.getAttributes().getNamedItem("sourcepath") != null) {
                        resultFileSet.add(resolveVariantPath(node.getAttributes().getNamedItem("sourcepath").getNodeValue()));
                    }
                } else if ("lib".equals(kind)) {
                    if (node.getAttributes().getNamedItem("path") != null) {
                        resultFileSet.add(new File(basePath, node.getAttributes().getNamedItem("path").getNodeValue()).getAbsolutePath());
                    }
                } else if ("con".equals(kind)) {
                    // IGNORE
                } else if ("output".equals(kind)) {
                    // IGNORE
                } else {
                    LogUtil.warn("Not supported kind: " + kind);
                }
            }
            return StringUtil.join(resultFileSet, Environment.LINE_SEPARATOR);
        } else {
            return content;
        }
    }

    private static String resolveVariantPath(String path) {
        if (path.startsWith("file:")) return path;
        if (path.startsWith("/")) return path;

        int indexOfLeftSlash = path.indexOf('/');
        int indexOfRightSlash = path.indexOf('\\');

        if (indexOfLeftSlash < 0 && indexOfRightSlash < 0) return path;

        int inexOfFirstSlash = Math.min(indexOfLeftSlash, indexOfRightSlash);
        if (inexOfFirstSlash < 0) {
            inexOfFirstSlash = Math.max(indexOfLeftSlash, indexOfRightSlash);
        }

        String variant = path.substring(0, inexOfFirstSlash);
        path = path.substring(variant.length() + 1);
        File variantValue;
        if ("M2_REPO".equals(variant)) {
            variantValue = new File(Environment.USER_HOME, ".m2/repository");
        } else {
            String vv = UnixArgsutil.ARGS.kvalue("X" + variant);
            if (vv == null) {
                throw new RuntimeException("Variant '" + variant + "' cannot found!f");
            }
            variantValue = new File(resolveUserPath(vv));
        }
        return new File(variantValue, path).getAbsolutePath();
    }

    private static String resolveUserPath(String path) {
        if (path == null) return path;
        if (path.startsWith("~")) {
            path = Environment.USER_HOME + path.substring(1);
        }
        return path;
    }

    private static boolean isPathMattch(Resource resource) {
        if (ffList.isEmpty()) {
            return true;
        }
        String fstr = resource.getResourceId().toString();
        for (MatchPattern matchPattern : ffList) {
            if (matchPattern.isInclude) {
                if (!matchPattern.matcher.match(fstr)) {
                    return false;
                }
            } else {
                if (matchPattern.matcher.match(fstr)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Matcher getMatcher(String search) {
        boolean is_i = UnixArgsutil.ARGS.flags().contains("i");
        boolean is_E = UnixArgsutil.ARGS.flags().contains("E");

        boolean has_is_i = false;
        String has = null;
        if (UnixArgsutil.ARGS.keys().contains("HAS")) {
            has = UnixArgsutil.ARGS.kvalue("HAS");
        } else if (UnixArgsutil.ARGS.keys().contains("has")) {
            has_is_i = true;
            has = UnixArgsutil.ARGS.kvalue("has");
        }

        Matcher parent = null;
        if (has != null) {
            final String _has = has_is_i ? has.toLowerCase() : has;
            final boolean _has_is_i = has_is_i;
            parent = new Matcher() {

                public boolean match(String line) {
                    String ln = _has_is_i ? line.toLowerCase() : line;
                    if (!ln.contains(_has)) {
                        return false;
                    }
                    return true;
                }
            };
        }
        return (is_E) ? new RegexMatcher(parent, search, is_i) : new ContainsMatcher(parent, search, is_i);
    }

    private static Set<String> getExtSet() {
        String f = UnixArgsutil.ARGS.kvalue("f");
        if ("ALL".equals(f)) {
            return null;
        }

        f = ((f == null) || f.trim().isEmpty()) ? "c,h,hpp,cpp,java" : f;

        Set<String> extSet = new HashSet<String>();
        String[] fs = f.split(",");
        for (String ff : fs) {
            ff = ff.trim().toLowerCase();
            if (!ff.isEmpty()) {
                extSet.add(ff);
            }
        }
        if (UnixArgsutil.ARGS.flags().contains("J")
            || StringUtil.notNull(UnixArgsutil.ARGS.kvalue("I")).endsWith(".classpath")) {
            extSet.add("jar");
            extSet.add("war");
            extSet.add("sar");
            extSet.add("zip");
        }
        return extSet;
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar findingall.jar [options] <text>");
        System.out.println("    -f <option>                  option(default [c,h,cpp,hpp,java])");
        System.out.println("       ALL                       all file(s)");
        System.out.println("       c                         .c file(s)");
        System.out.println("       h                         .h file(s)");
        System.out.println("       cpp                       .cpp file(s)");
        System.out.println("       hpp                       .hpp file(s)");
        System.out.println("       java                      .java file(s)");
        System.out.println("    -I <file>                    file name(s) from input file");
        System.out.println("    -o <charset>                 console output charset");
        System.out.println("    -has <symbol>                only the line has symbol(case insensitive, -HAS case sensitive)");
        System.out.println("    -ff <filter>                 file and path filter(regex, starts with '~' means exclude)");
        System.out.println("    -CC                          concurrent thread(s) count");
        System.out.println("    --i                          ignore case contains");
        System.out.println("    --E                          regex");
        System.out.println("    --e                          ignore case regex");
        System.out.println("    --0                          only print file name");
        System.out.println("    --1                          one file matches only one time");
        System.out.println("    --s                          print simple file name");
        System.out.println("    --F                          print full file name");
        System.out.println("    --N                          print match at new line");
        System.out.println("    --C                          color print");
        System.out.println("    --L                          line number print");
        System.out.println("    --J                          match jar and zip file(s)");
        System.exit(0);
    }
}
