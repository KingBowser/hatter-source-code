package me.hatter.tools.svndiff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.hatter.tools.commons.args.UnixArgsUtil;
import me.hatter.tools.commons.process.ProcessTool;

public class Main {

    private static final String defaultSvnEncoding = "GBK";
    private static String       svnEncode;
    private static String       outEncode;

    public static void main(String[] args) {
        try {
            UnixArgsUtil.parseGlobalArgs(args);

            svnEncode = UnixArgsUtil.ARGS.kvalue("svncs");
            outEncode = UnixArgsUtil.ARGS.kvalue("outcs");

            if (UnixArgsUtil.ARGS.args().length == 0) {
                System.err.println("Please input svn url.");
                System.err.println("Usage:");
                System.err.println("svn_diff [-svncs GBK] [-outcs UTF8] <svn_url>");
                System.exit(-1);
            }

            String rev = parseRevision(getSvnLog(UnixArgsUtil.ARGS.args()));
            System.out.println("REVISION: " + rev);

            if (rev == null) {
                throw new RuntimeException("Cannot find svn log.");
            }

            String diff = getSvnDiff(UnixArgsUtil.ARGS.args(), rev);
            if (outEncode == null) {
                System.out.print(diff);
            } else {
                System.out.write(diff.getBytes(outEncode));
            }
            System.out.println();

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static String parseRevision(String log) throws IOException {
        BufferedReader sr = new BufferedReader(new StringReader(log));

        String rev = null;
        Pattern P = Pattern.compile("(r\\d+)\\s+\\|\\s+.*");
        for (String line; (line = sr.readLine()) != null;) {
            Matcher m = P.matcher(line);
            if (m.matches()) {
                rev = m.group(1);
            }
        }

        return rev;
    }

    public static String getSvnDiff(String[] args, String rev) throws UnsupportedEncodingException {
        List<String> cmd = new ArrayList<String>(Arrays.asList("svn", "di", "-" + rev + ":HEAD"));
        cmd.addAll(Arrays.asList(args));

        ProcessTool pt = new ProcessTool();
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        byte[] result = pt.callProcess(processBuilder);
        return new String(result, (svnEncode == null ? defaultSvnEncoding : svnEncode));
    }

    public static String getSvnLog(String[] args) throws UnsupportedEncodingException {
        List<String> cmd = new ArrayList<String>(Arrays.asList("svn", "log", "--stop-on-copy"));
        cmd.addAll(Arrays.asList(args));

        ProcessTool pt = new ProcessTool();
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        byte[] result = pt.callProcess(processBuilder);
        return new String(result, (svnEncode == null ? defaultSvnEncoding : svnEncode));
    }
}
