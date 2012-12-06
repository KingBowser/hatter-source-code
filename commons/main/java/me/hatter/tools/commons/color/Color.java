package me.hatter.tools.commons.color;

import java.util.Arrays;
import java.util.List;

public class Color {

    public static final Color BG_RED   = getColor(101);
    public static final Color BG_GREEN = getColor(102);

    private List<Integer>     values;

    public Color(Integer... value) {
        this.values = Arrays.asList(value);
    }

    public static Color getColor(Integer... value) {
        return new Color(value);
    }

    public List<Integer> getValues() {
        return this.values;
    }
}
