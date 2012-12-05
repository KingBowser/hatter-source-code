package atests;

import jline.Terminal;
import jline.TerminalFactory;
import me.hatter.tools.commons.color.Font;
import me.hatter.tools.commons.color.Position;
import me.hatter.tools.commons.screen.TermUtils;
import me.hatter.tools.commons.string.StringUtil;

public class ClearAndOutput {

    public static void main(String[] args) {
        System.out.print(TermUtils.CLEAR);
        System.out.print(Font.createFont(Position.getPosition(1, 1), null).display(""));
        System.out.print(TermUtils.MOVE_LEFT1);
        Terminal t = TerminalFactory.create();
        
        System.out.println(StringUtil.repeat("a", t.getWidth()));
        System.out.println(StringUtil.repeat("b", t.getWidth()));
        System.out.println("helloworld");
        System.out.println("testing...");
    }
}
