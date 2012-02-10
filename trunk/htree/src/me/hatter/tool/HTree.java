package me.hatter.tool;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class HTree {

    static class Flags {

        public boolean size;
        public String  unit;
        public boolean date;
        public String  format;
        public String  filter;
    }

    static enum GetInFlag {
        Y, N
    }

    static class GetInStack extends Stack<GetInFlag> {

        private static final long serialVersionUID = 1L;
    }

    private static String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        args = new String[] { "-size", "-unit/X", "-date", "-format/yyyy/MM/dd HH:mm:ss", "/Users" };

        Flags flags = new Flags();
        args = ArgsUtil.mappingArgs(args, flags);
        String dir = (args.length > 0) ? args[0].trim() : "";
        File dirFile = getDirFile(dir);

        GetInStack refGetInStack = new GetInStack();
        try {
            System.out.println(dirFile.getCanonicalPath());
        } catch (IOException e) {
            // IGNORE
        }
        showFiles(dirFile, refGetInStack, flags);
    }

    private static void showFiles(File file, GetInStack refGetInStack, Flags flags) {
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                boolean isLast = (i == (files.length - 1));
                String cprefix = (isLast ? "`" : "|") + "-- ";
                StringBuilder line = new StringBuilder(getPrefix(refGetInStack) + cprefix);
                if (f.isDirectory()) {
                    line.append(f.getName());
                    System.out.println(line.toString());
                    refGetInStack.push(isLast ? GetInFlag.N : GetInFlag.Y);
                    showFiles(f, refGetInStack, flags);
                    refGetInStack.pop();
                } else {
                    line.append(f.getName());
                    if (flags.size) {
                        line.append(" [" + getSize(f, flags.unit) + "]");
                    }
                    if (flags.date) {
                        line.append(" #" + getDate(file, flags.format));
                    }
                    System.out.println(line.toString());
                }
            }
        }
    }

    private static String getPrefix(GetInStack refGetInStack) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < refGetInStack.size(); i++) {
            sb.append((refGetInStack.get(i) == GetInFlag.Y) ? "|" : " ");
            sb.append("   ");
        }
        return sb.toString();
    }

    private static File getDirFile(String dir) {
        File dirFile = null;
        if (dir.length() > 0) {
            if (dir.startsWith("/")) { // is abstract path
                dirFile = new File(dir);
            } else {
                dirFile = new File(USER_DIR, dir);
            }
        } else {
            dirFile = new File(USER_DIR);
        }
        return dirFile;
    }

    private static String getDate(File file, String format) {
        SimpleDateFormat sdf;
        if (format == null) {
            sdf = new SimpleDateFormat();
        } else {
            try {
                sdf = new SimpleDateFormat(format);
            } catch (Exception e) {
                System.err.println("[ERROR] Error in create date format: " + e.getMessage());
                if (System.getProperties().containsKey("program.trace")) {
                    e.printStackTrace();
                }
                sdf = new SimpleDateFormat();
            }
        }
        Date lastModifed = new Date(file.lastModified());
        return sdf.format(lastModifed);
    }

    private static long K = 1024;
    private static long M = 1024 * 1024;
    private static long G = 1024 * 1024 * 1024;

    private static String getSize(File file, String unit) {
        long flen = file.length();
        if ("X".equalsIgnoreCase(unit)) {
            if (flen > G) {
                unit = "G";
            } else if (flen > M) {
                unit = "M";
            } else if (flen > K) {
                unit = "K";
            }
        }
        if ("K".equalsIgnoreCase(unit)) {
            return String.valueOf(flen / K) + "K";
        } else if ("M".equalsIgnoreCase(unit)) {
            return String.valueOf(flen / M) + "M";
        } else if ("G".equalsIgnoreCase(unit)) {
            return String.valueOf(flen / G) + "G";
        } else {
            return String.valueOf(flen);
        }
    }
}
