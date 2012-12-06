package me.hatter.tools.commons.screen;

// echo -e '\033[1;4;101mabc\033[0m'
// http://www.cnblogs.com/mugua/archive/2009/11/25/1610118.html
// http://hi.baidu.com/vivo01/item/f308cf8eb9d387814514cf58
public class TermUtils {

    public static final char   CHAR_27     = (char) 27;
    public static final String RESET       = CHAR_27 + "[0m";
    public static final String CLEAR       = CHAR_27 + "[2J";
    public static final String MOVE_LEFT1  = CHAR_27 + "[1D";
    public static final String MOVE_RIGHT1 = CHAR_27 + "[1C";
    public static final String MOVE_UP1    = CHAR_27 + "[1A";
    public static final String MOVE_DOWN1  = CHAR_27 + "[1B";

    public static final int    DEFAULT     = 0;
    public static final int    BOLD        = 1;
    public static final int    UNDERLINE   = 4;
    public static final int    BLINK       = 5;

    public static interface Fore {

        int BLANK                 = 30;
        int RED                   = 31;
        int GREEN                 = 32;
        int BROWN                 = 33;
        int BLUE                  = 34;
        int PINK                  = 35;
        int PEACOCK_BLUE          = 36;
        int WHITE                 = 37;
        int DEFAULT_UNDERLINE_ON  = 38;
        int DEFAULT_UNDERLINE_OFF = 39;
    }

    public static interface Back {

        int BLANK        = 40;
        int RED          = 41;
        int GREEN        = 42;
        int BROWN        = 43;
        int BLUE         = 44;
        int PINK         = 45;
        int PEACOCK_BLUE = 46;
        int WHITE        = 47;
        int DEFAULT      = 49;
    }

    public static interface XFore {

        int BLANK        = 90;
        int RED          = 91;
        int GREEN        = 92;
        int BROWN        = 93;
        int BLUE         = 94;
        int PINK         = 95;
        int PEACOCK_BLUE = 96;
        int WHITE        = 97;
    }

    public static interface XBack {

        int BLANK        = 100;
        int RED          = 101;
        int GREEN        = 102;
        int BROWN        = 103;
        int BLUE         = 104;
        int PINK         = 105;
        int PEACOCK_BLUE = 106;
        int WHITE        = 107;
    }
}
