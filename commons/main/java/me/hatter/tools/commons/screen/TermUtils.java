package me.hatter.tools.commons.screen;

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
}
