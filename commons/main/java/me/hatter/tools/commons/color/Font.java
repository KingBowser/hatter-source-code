package me.hatter.tools.commons.color;

public class Font {

    private Color   color;
    private boolean isBold;

    public Font(Color color) {
        this(color, false);
    }

    public Font(Color color, boolean isBold) {
        this.color = color;
        this.isBold = isBold;
    }

    public static Font createFont(Color color) {
        return createFont(color, false);
    }

    public static Font createFont(Color color, boolean isBold) {
        return new Font(color, isBold);
    }

    public String wrap(String text) {
        if (Color.getColorValue(color) == null) {
            return text;
        }
        return Color.CHAR_27 + "["//
               + (isBold ? "1" : "0") //
               + ";" + color.getValue() //
               + "m" + text + Color.RESET;
    }
}
