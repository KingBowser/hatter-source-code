package me.hatter.tools.commons.color;

public class Color {

    public static final char   CHAR_27 = (char) 27;
    public static final String RESET   = CHAR_27 + "[0m";
    public static final String CLEAR   = CHAR_27 + "[2J";

    private Integer            value;

    public Color(Integer value) {
        this.value = value;
    }

    public static Color getColor(Integer value) {
        return new Color(value);
    }

    public static Integer getColorValue(Color color) {
        return (color == null) ? null : color.getValue();
    }

    public Integer getValue() {
        return this.value;
    }
}
