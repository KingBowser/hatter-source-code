package me.hatter.tools.jtop.util.console;

public class Font {

    private Color   color;
    private Color   bgColor;
    private boolean isBold;

    public Font(Color color, boolean isBold) {
        this(color, null, isBold);
    }

    public Font(Color color, Color bgColor, boolean isBold) {
        this.color = color;
        this.bgColor = bgColor;
        this.isBold = isBold;
    }

    public static Font createFont(Color color, boolean isBold) {
        return createFont(color, null, isBold);
    }

    public static Font createFont(Color color, Color bgColor, boolean isBold) {
        return new Font(color, bgColor, isBold);
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
        return ConsoleConstants.CHAR_27 //
               + "[" + (isBold ? "1" : "0") //
               + ";" + color.getValue() //
               + ((bgColor == null) ? "" : ";" + bgColor.getValue()) //
               + "m";
    }
}
