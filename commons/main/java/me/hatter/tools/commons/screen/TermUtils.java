package me.hatter.tools.commons.screen;

public class TermUtils {

    public static final char   CHAR_27    = (char) 27;
    public static final String RESET      = CHAR_27 + "[0m";
    public static final String CLEAR      = CHAR_27 + "[2J";
    public static final String MOVE_LEFT1 = CHAR_27 + "[1D";
}
