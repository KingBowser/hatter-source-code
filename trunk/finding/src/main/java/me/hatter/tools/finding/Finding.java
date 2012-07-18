package me.hatter.tools.finding;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import me.hatter.tools.commons.args.UnixArgsutil;
import me.hatter.tools.commons.concurrent.ExecutorUtil;
import me.hatter.tools.commons.environment.Environment;
import me.hatter.tools.commons.file.FileUtil;
import me.hatter.tools.commons.io.StringBufferedReader;
import me.hatter.tools.commons.io.StringPrintWriter;
import me.hatter.tools.commons.number.IntegerUtil;
import me.hatter.tools.commons.string.StringUtil;

public class Finding {

    public static final char   CHAR_27 = (char) 27;
    public static final String RESET   = CHAR_27 + "[0m";

    public interface MatchFileFilter extends FileFilter {

        void matchFile(File file);
    }

    public static void main(String[] args) {
        UnixArgsutil.parseGlobalArgs(args);
        if (UnixArgsutil.ARGS.args().length == 0) {
            usage();
        }

        final Set<String> extSet = getExtSet();
        final String search = UnixArgsutil.ARGS.args()[0];
        final Matcher matcher = getMatcher(search);
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

        final MatchFileFilter matchFileFilter = new MatchFileFilter() {

            public void matchFile(File file) {

                if (file.isDirectory()) {
                    return; // only match file
                }
                if (extSet != null) {
                    String ext = StringUtil.substringAfterLast(file.getAbsolutePath(), ".");
                    if (ext == null) {
                        return;
                    }
                    if (!extSet.contains(ext.toLowerCase())) {
                        return;
                    }
                }

                fileCount.incrementAndGet();
                int mcount = 0;
                int linenumber = 0;
                String text = FileUtil.readFileToString(file);
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
                            fn = file.getAbsolutePath();
                        } else if (is_s) {
                            fn = file.getName();
                        } else {
                            fn = "." + file.getAbsolutePath().replace(Environment.USER_DIR, StringUtil.EMPTY);
                            fn = (fn.startsWith("././")) ? fn.substring(2) : fn;
                        }
                        fn = fileColorSt + fn + colorEd;

                        String outln = ln;
                        if (is_C && (matcher instanceof ContainsMatcher)) {
                            outln = StringUtil.EMPTY;
                            boolean _is_i = UnixArgsutil.ARGS.flags().contains("i");
                            String mln = _is_i ? ln.toLowerCase() : ln;
                            String oln = ln;
                            String mse = _is_i ? search : search;
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
                        System.out.print(printWriter.toString());

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

            public boolean accept(final File file) {

                if (file.isDirectory()) {
                    return (!file.toString().contains(".svn"));
                }
                executor.submit(new Runnable() {

                    public void run() {
                        matchFile(file);
                    }
                });
                return false;
            }
        };

        final File dir = new File(Environment.USER_DIR);

        if (UnixArgsutil.ARGS.keys().contains("I")) {
            File inf = new File(UnixArgsutil.ARGS.kvalue("I"));
            String files = FileUtil.readFileToString(inf);

            StringBufferedReader reader = new StringBufferedReader(files);
            for (String file; ((file = reader.readOneLine()) != null);) {
                final File f_file = new File(file);
                executor.submit(new Runnable() {

                    public void run() {
                        matchFileFilter.matchFile(f_file);
                    }
                });
            }
        } else {
            FileUtil.listFiles(dir, matchFileFilter, null);
        }

        executor.shutdown();

        final long endMillis = System.currentTimeMillis();
        DecimalFormat format = new DecimalFormat("#,###,###");
        System.out.println("Finish, Total: " + format.format(totalCount.get()) + ", Ext Match: "
                           + format.format(fileCount.get()) + ", Txt Match: " + format.format(matchCount.get())
                           + ", Cost: " + format.format(endMillis - startMillis) + " ms");
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

    public static interface Matcher {

        boolean match(String line);
    }

    public static class ContainsMatcher implements Matcher {

        private Matcher parent;
        private String  search;
        private boolean ignoreCase;

        public ContainsMatcher(Matcher parent, String search, boolean ignoreCase) {
            this.parent = parent;
            this.search = ignoreCase ? search.toLowerCase() : search;
            this.ignoreCase = ignoreCase;
        }

        public boolean match(String line) {
            if (line == null) return false;
            if ((parent != null) && (!parent.match(line))) return false;
            return (ignoreCase) ? line.toLowerCase().contains(search) : line.contains(search);
        }
    }

    public static class RegexMatcher implements Matcher {

        private Matcher parent;
        private Pattern search;

        public RegexMatcher(Matcher parent, String regex, boolean ignoreCase) {
            this.parent = parent;
            regex = (regex.startsWith("^")) ? regex : (".*" + regex);
            regex = (regex.endsWith("$")) ? regex : (regex + ".*");
            if (ignoreCase) {
                this.search = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            } else {
                this.search = Pattern.compile(regex);
            }
        }

        public boolean match(String line) {
            if (line == null) return false;
            if ((parent != null) && (!parent.match(line))) return false;
            return search.matcher(line).matches();
        }
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
        System.out.println("    -has <symbol>                only the line has symbol(case insensitive)");
        System.out.println("    -HAS <symbol>                only the line has symbol(case sensitive)");
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
        System.exit(0);
    }
}
