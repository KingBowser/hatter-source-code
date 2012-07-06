package me.hatter.tools.commons.bytes;

import java.text.DecimalFormat;

import me.hatter.tools.commons.string.StringUtil;

public class ByteUtil {

    public static final long N1024 = 1024L;

    public static enum ByteFormat {
        BYTE(1L, null),

        KB(N1024, "K"),

        MB(N1024 * N1024, "M"),

        GB(N1024 * N1024 * N1024, "G"),

        HUMAN(0L, null);

        private String suffix;
        private long   size;

        public long getSize() {
            return size;
        }

        public String getSuffix() {
            return suffix;
        }

        public static ByteFormat fromString(String str) {
            if ((str == null) || (str.length() == 0)) {
                return null;
            }
            char c = str.toLowerCase().charAt(0);
            switch (c) {
                case 'h':
                    return ByteFormat.HUMAN;
                case 'b':
                    return ByteFormat.BYTE;
                case 'k':
                    return ByteFormat.KB;
                case 'm':
                    return ByteFormat.MB;
                case 'g':
                    return ByteFormat.GB;
                default:
                    return null;
            }
        }

        private ByteFormat(long size, String suffix) {
            this.size = size;
            this.suffix = suffix;
        }
    }

    public static String formatBytes(ByteFormat format, long size) {
        if (format == null) {
            return String.valueOf(size);
        }
        boolean isMinus = (size < 0);
        size = Math.abs(size);
        DecimalFormat nf = new DecimalFormat("0.00");
        if (format == ByteFormat.HUMAN) {
            if (size > ByteFormat.GB.getSize()) {
                format = ByteFormat.GB;
            } else if (size > ByteFormat.MB.getSize()) {
                format = ByteFormat.MB;
            } else if (size > ByteFormat.KB.getSize()) {
                format = ByteFormat.KB;
            } else {
                format = ByteFormat.BYTE;
            }
        }
        String prefix = isMinus ? "-" : StringUtil.EMPTY;
        switch (format) {
            case KB:
                return prefix + nf.format(((double) size) / ByteFormat.KB.getSize()) + ByteFormat.KB.getSuffix();
            case MB:
                return prefix + nf.format(((double) size) / ByteFormat.MB.getSize()) + ByteFormat.MB.getSuffix();
            case GB:
                return prefix + nf.format(((double) size) / ByteFormat.GB.getSize()) + ByteFormat.GB.getSuffix();
            default:
                return prefix + String.valueOf(size);
        }
    }
}
