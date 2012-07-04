package me.hatter.tools.jtop.util.console;


public class Text {

    private Font   font;
    private String text;

    public Text(Font font, String text) {
        super();
        this.font = font;
        this.text = text;
    }

    public static Text createText(Font font, String text) {
        return new Text(font, text);
    }

    public static Text createText(Color color, String text) {
        return createText(color, false, text);
    }

    public static Text createText(Color color, boolean isBold, String text) {
        if (color == null) {
            return new Text(null, text);
        } else {
            return new Text(Font.createFont(color, isBold), text);
        }
    }

    public String toString() {
        return display();
    }

    public String display() {
        if (font == null) {
            return text;
        } else {
            return font.getFont() + text + ConsoleConstants.RESET;
        }
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
}
