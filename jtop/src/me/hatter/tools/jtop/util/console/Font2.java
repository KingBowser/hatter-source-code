package me.hatter.tools.jtop.util.console;

import me.hatter.tools.commons.screen.TermUtils;

public class Font2 {

    private Color2   color;
    private Color2   bgColor;
    private boolean isBold;

    public Font2(Color2 color, boolean isBold) {
        this(color, null, isBold);
    }

    public Font2(Color2 color, Color2 bgColor, boolean isBold) {
        this.color = color;
        this.bgColor = bgColor;
        this.isBold = isBold;
    }

    public static Font2 createFont(Color2 color, boolean isBold) {
        return createFont(color, null, isBold);
    }

    public static Font2 createFont(Color2 color, Color2 bgColor, boolean isBold) {
        return new Font2(color, bgColor, isBold);
    }

    // #define BLACK \"\33[22;30m\"
    // #define GRAY \"\33[01;30m\"
    // #define RED \"\33[22;31m\"
    // #define LRED \"\33[01;21m\"
    // #define GREEN \"\33[22;32m\"
    // #define LGREEN \"\33[01;32m\"
    // #define BLUE \"\33[22;34m\"
    // #define LBLUE \"\33[01;34m\"
    // #define BROWN \"\33[22;33m\"
    // #define YELLOW \"\33[01;33m\"
    // #define CYAN \"\33[22;36m\"
    // #define LCYAN \"\33[22;36m\"
    // #define MAGENTA \"\33[22;35m\"
    // #define LMAGENTA \"\33[01;35m\"
    // #define NC \"\33[0m\"
    // #define BOLD \"\33[1m\"
    // #define ULINE \"\33[4m\" //underline
    // #define BLINK \"\33[5m\"
    // #define INVERT \"\33[7m\"

    public String getFont() {
        return TermUtils.CHAR_27 //
               + "[" + (isBold ? "1" : "0") //
               + ";" + color.getValue() //
               + ((bgColor == null) ? "" : ";" + bgColor.getValue()) //
               + "m";
    }
}
