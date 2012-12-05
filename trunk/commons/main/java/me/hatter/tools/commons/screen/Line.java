package me.hatter.tools.commons.screen;

import java.util.ArrayList;

import me.hatter.tools.commons.color.Text;
import me.hatter.tools.commons.string.StringUtil;

public class Line extends ArrayList<Text> {

    private static final long serialVersionUID = -8468571335422880092L;

    public String toString() {
        return StringUtil.join(toArray(), StringUtil.EMPTY);
    }
}
