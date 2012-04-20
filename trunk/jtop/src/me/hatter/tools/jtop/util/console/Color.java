package me.hatter.tools.jtop.util.console;

public enum Color {

    DEFAULT(0),

    RED(31),

    BLUE(34),

    CYAN(36),

    GREEN(32),

    YELLOW(33),

    WHITE(37),

    BLACK(30);

    private int value;

    private Color(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
