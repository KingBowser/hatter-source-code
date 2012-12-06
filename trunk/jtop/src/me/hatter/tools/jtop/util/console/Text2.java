package me.hatter.tools.jtop.util.console;

import me.hatter.tools.commons.screen.TermUtils;

public class Text2 {

    private Font2   font;
    private String text;

    public Text2(Font2 font, String text) {
        super();
        this.font = font;
        this.text = text;
    }

    public static Text2 createText(Font2 font, String text) {
        return new Text2(font, text);
    }

    public static Text2 createText(Color2 color, String text) {
        return createText(color, false, text);
    }

    public static Text2 createText(Color2 color, boolean isBold, String text) {
        if (color == null) {
            return new Text2(null, text);
        } else {
            return new Text2(Font2.createFont(color, isBold), text);
        }
    }

    public String toString() {
        return display();
    }

    public String display() {
        if (font == null) {
            return text;
        } else {
            return font.getFont() + text + TermUtils.RESET;
        }
    }

    public Font2 getFont() {
        return font;
    }

    public void setFont(Font2 font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
