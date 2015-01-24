package me.hatter.tools.commons.number;

public class NumberUtil {

    private static final String numbers = "0123456789" //
                                          + "abcdefghijklmnopqrstuvwxyz" //
                                          + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" //
                                        ;

    public static long decompress(String s) {
        if (s == null) {
            return 0;
        }
        long n = 0;
        int C = numbers.length();
        int L = s.length();
        for (int i = (L - 1); i >= 0; i--) {
            int x = numbers.indexOf(new String(new char[] { s.charAt(i) }));
            if (x < 0) {
                throw new IllegalArgumentException("Cannot parse: " + s);
            }
            n += power(C, (L - i - 1)) * x;
        }
        return n;
    }

    public static long power(int v, int p) {
        long ret = 1;
        for (int i = 0; i < p; i++) {
            ret *= v;
        }
        return ret;
    }

    public static String compress(long n) {
        if (n == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        int C = numbers.length();
        while (n > 0) {
            sb.append(numbers.charAt((int) (n % C)));
            n = n / C;
        }
        return sb.reverse().toString();
    }
}
