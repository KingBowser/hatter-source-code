package me.hatter.tools.commons.color;

import java.util.LinkedHashSet;
import java.util.Set;

import me.hatter.tools.commons.screen.TermUtils;
import me.hatter.tools.commons.string.StringUtil;

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
        if ((color != null) && (color.getValues() != null) && (color.getValues().size() > 0)) {
            needEndFlag = true;
            Set<Integer> l = new LinkedHashSet<Integer>();
            if (isBold) {
                l.add(1);
            }
            l.addAll(color.getValues());
            sb.append(TermUtils.CHAR_27 + "[" + StringUtil.join(l.toArray(), ";") + "m");
        }
        sb.append(text);
        if (needEndFlag) {
            sb.append(TermUtils.RESET);
        }
        return sb.toString();
    }
}
