package me.hatter.tools.commons.color;

import me.hatter.tools.commons.string.StringUtil;

public class Text {

    private Font   font;
    private String text;

    public Text(Font font, String text) {
        this.font = font;
        this.text = (text == null) ? StringUtil.EMPTY : text;
    }

    public static Text createText(Font font, String text) {
        return new Text(font, text);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        if (font == null) {
            return text;
        }
        return font.display(text);
    }
}
