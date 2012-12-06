package me.hatter.tools.commons.color;

public class Color {

    public static final Color BG_RED   = getColor(101);
    public static final Color BG_GREEN = getColor(102);

    private Integer           value;

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
