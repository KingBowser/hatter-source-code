package me.hatter.tools.commons.color;

import me.hatter.tools.commons.screen.TermUtils;

public class Font {

    private Position position;
    private Color    color;
    private boolean  isBold;

    public Font(Color color) {
        this(null, color, false);
    }

    public Font(Position position, Color color) {
        this(position, color, false);
    }

    public Font(Position position, Color color, boolean isBold) {
        this.position = position;
        this.color = color;
        this.isBold = isBold;
    }

    public static Font createFont(Color color) {
        return createFont(color, false);
    }

    public static Font createFont(Position position, Color color) {
        return createFont(position, color, false);
    }

    public static Font createFont(Color color, boolean isBold) {
        return createFont(null, color, isBold);
    }

    public static Font createFont(Position position, Color color, boolean isBold) {
        return new Font(position, color, isBold);
    }

    public String display(String text) {
        StringBuffer sb = new StringBuffer();
        boolean needEndFlag = false;
        if (position != null) {
            needEndFlag = true;
            sb.append(TermUtils.CHAR_27 + "[" + position.getRow() + "H");
            sb.append(TermUtils.CHAR_27 + "[" + position.getCol() + "C");
        }
        if (Color.getColorValue(color) != null) {
            needEndFlag = true;
            String bold = isBold ? "1" : "0";
            sb.append(TermUtils.CHAR_27 + "[" + bold + ";" + color.getValue() + "m");
        }
        sb.append(text);
        if (needEndFlag) {
            sb.append(TermUtils.RESET);
        }
        return sb.toString();
    }
}
