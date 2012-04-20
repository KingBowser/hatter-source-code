package me.hatter.tools.jtop.util.console;

public enum Color {

    RED(31),

    BLUE(34),

    CYAN(36),

    GREEN(32),

    YELLOW(33);

    private int value;

    private Color(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
