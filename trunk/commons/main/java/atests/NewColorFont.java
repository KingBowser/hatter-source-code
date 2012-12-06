package atests;

import me.hatter.tools.commons.color.Color;
import me.hatter.tools.commons.color.Font;
import me.hatter.tools.commons.screen.TermUtils;

public class NewColorFont {

    public static void main(String[] args) {
        System.out.println(Font.createFont(null, Color.getColor(TermUtils.Fore.GREEN, TermUtils.Back.RED), false).display("hello world"));
        System.out.println(Font.createFont(null, Color.getColor(TermUtils.XFore.GREEN, TermUtils.XBack.RED), false).display("hello world"));
        System.out.println(Font.createFont(null, Color.getColor(TermUtils.BLINK, TermUtils.XBack.GREEN), true).display("hello world"));
    }
}
