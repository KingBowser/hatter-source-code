package me.hatter.tools.finding;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.StringBufferedReader;
import me.hatter.tools.commons.string.StringUtil;

public class Finding {

    public static final char   CHAR_27 = (char) 27;
    public static final String RESET   = CHAR_27 + "[0m";

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);
        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }

        final Set<String> extSet = getExtSet();
        final String search = UnixArgsutil.ARGS.args()[0];
        final Matcher matcher = getMatcher(search);
        final boolean is_1 = UnixArgsutil.ARGS.flags().contains("1");
        final boolean is_s = UnixArgsutil.ARGS.flags().contains("s");
        final boolean is_F = UnixArgsutil.ARGS.flags().contains("F");
        final boolean is_N = UnixArgsutil.ARGS.flags().contains("N");
        final boolean is_C = UnixArgsutil.ARGS.flags().contains("C");
        final long startMillis = System.currentTimeMillis();
        final AtomicLong totalCount = new AtomicLong(0);
        final AtomicLong matchCount = new AtomicLong(0);
        final File dir = new File(Environment.USER_DIR);
        FileUtil.listFiles(dir, new FileFilter() {

            public boolean accept(File file) {

                if (file.isDirectory()) {
                    return (!file.toString().contains(".svn"));
                }
                if (extSet != null) {
                    String ext = StringUtil.substringAfterLast(file.getAbsolutePath(), ".");
                    if (ext == null) {
                        return false;
                    }
                    if (!extSet.contains(ext.toLowerCase())) {
                        return false;
                    }
                }
                int mcount = 0;
                String text = FileUtil.readFileToString(file, "UTF-8");
                StringBufferedReader reader = new StringBufferedReader(text);
                for (String line; ((line = reader.readOneLine()) != null);) {
                    totalCount.incrementAndGet();
                    boolean is_match = matcher.match(line);
                    if (is_match) {
                        if (is_1) {
                            return false;
                        }
                        if (mcount == 0) {
                            matchCount.incrementAndGet();
                        }
                        String fileColorSt = is_C ? (CHAR_27 + "[;32m") : "";
                        String colorEd = is_C ? RESET : "";
                        String fn;
                        if (is_F) {
                            fn = file.getAbsolutePath();
                        } else if (is_s) {
                            fn = file.getName();
                        } else {
                            fn = "." + file.getAbsolutePath().replace(Environment.USER_DIR, StringUtil.EMPTY);
                        }
                        fn = fileColorSt + fn + colorEd;
                        String ln = line.trim();
                        if (is_N) {
                            if (mcount == 0) {
                                System.out.println(fn);
                            }
                            System.out.println("\t: " + ln);
                        } else {
                            System.out.println(fn + ": " + ln);
                        }
                        mcount++;
                    }
                }
                return false;
            }
        }, null);
        final long endMillis = System.currentTimeMillis();
        DecimalFormat format = new DecimalFormat("#,###,###");
        System.out.println("Finish, Total: " + format.format(totalCount.get()) + ", Match: "
                           + format.format(matchCount.get()) + ", Cost: " + format.format(endMillis - startMillis)
                           + " ms");
    }

    private static Matcher getMatcher(String search) {
        boolean is_i = UnixArgsutil.ARGS.flags().contains("i");
        boolean is_E = UnixArgsutil.ARGS.flags().contains("E");
        boolean is_e = UnixArgsutil.ARGS.flags().contains("e");

        if (is_i) {
            return new ContainsMatcher(search, true);
        }
        if (is_E) {
            return new RegexMatcher(search, false);
        }
        if (is_e) {
            return new RegexMatcher(search, true);
        }

        return new ContainsMatcher(search, false);
    }

    public static interface Matcher {

        boolean match(String line);
    }

    public static class ContainsMatcher implements Matcher {

        private String  search;
        private boolean ignoreCase;

        public ContainsMatcher(String search, boolean ignoreCase) {
            this.search = ignoreCase ? search.toLowerCase() : search;
            this.ignoreCase = ignoreCase;
        }

        public boolean match(String line) {
            if (line == null) return false;
            return (ignoreCase) ? line.toLowerCase().contains(search) : line.contains(search);
        }
    }

    public static class RegexMatcher implements Matcher {

        private Pattern search;

        public RegexMatcher(String regex, boolean ignoreCase) {
            if (ignoreCase) {
                this.search = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            } else {
                this.search = Pattern.compile(regex);
            }
        }

        public boolean match(String line) {
            if (line == null) return false;
            return search.matcher(line).matches();
        }
    }

    private static Set<String> getExtSet() {
        String f = UnixArgsutil.ARGS.kvalue("f");
        if ("ALL".equals(f)) {
            return null;
        }

        f = ((f == null) || f.trim().isEmpty()) ? "c,h,hpp,cpp,java" : f;
        f = ("ch".equals(f)) ? "c,h" : f;
        f = ("chpp".equals(f)) ? "cpp,hpp" : f;

        Set<String> extSet = new HashSet<String>();
        String[] fs = f.split(",");
        for (String ff : fs) {
            ff = ff.trim().toLowerCase();
            if (!ff.isEmpty()) {
                extSet.add(ff);
            }
        }
        return extSet;
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  java -jar findingall.jar [options] <text>");
        System.out.println("    -f <option>                  option(default [c, h, cpp, hpp, java])");
        System.out.println("       ALL                       all file(s)");
        System.out.println("       c                         .c file(s)");
        System.out.println("       h                         .h file(s)");
        System.out.println("       ch                        .c, .h file(s)");
        System.out.println("       cpp                       .cpp file(s)");
        System.out.println("       hpp                       .hpp file(s)");
        System.out.println("       chpp                      .cpp, .hpp file(s)");
        System.out.println("       java                      .java file(s)");
        System.out.println("    --i                          ignore case contains");
        System.out.println("    --E                          regex");
        System.out.println("    --e                          ignore case regex");
        System.out.println("    --1                          one file matches only one time");
        System.out.println("    --s                          print simple file name");
        System.out.println("    --F                          print full file name");
        System.out.println("    --N                          print match at new line");
        System.out.println("    --C                          color print");
        System.exit(0);
    }
}
