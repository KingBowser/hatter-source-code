package me.hatter.tools.jtop.util.console;


public class Font {

    private Color   color;
    private boolean isBold;

    public Font(Color color, boolean isBold) {
        this.color = color;
        this.isBold = isBold;
    }

    public static Font createFont(Color color, boolean isBold) {
        return new Font(color, isBold);
    }

    public String getFont() {
        return ConsoleConstants.CHAR_27 + "[" + (isBold ? "1" : "0") + ";" + color.getValue() + "m";
    }
}
