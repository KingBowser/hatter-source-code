package me.hatter.tools.commons.screen.impl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import me.hatter.tools.commons.color.Font;
import me.hatter.tools.commons.color.Position;
import me.hatter.tools.commons.color.Text;
import me.hatter.tools.commons.screen.Printer;
import me.hatter.tools.commons.screen.TermUtils;
import me.hatter.tools.commons.string.StringUtil;

public class ScreenPrinter implements Printer {

    private PrintStream out;

    private int         width;
    private int         height;

    private int         chars = 0;
    private int         lines = 1;

    public ScreenPrinter(int width, int height) {
        this(System.out, width, height);
    }

    public ScreenPrinter(PrintStream out, int width, int height) {
        this.out = out;
        this.width = width;
        this.height = height;
    }

    @Override
    public void init() {
        // clean screen and move to POS:1,1
        out.println();
        out.print(TermUtils.CLEAR);
        out.print(Font.createFont(Position.getPosition(1, 1), null).display(StringUtil.EMPTY));
        out.print(TermUtils.MOVE_LEFT1);
    }

    @Override
    public void print(String str) {
        printStrOrText(str, null, false);
    }

    @Override
    public void print(Text text) {
        printStrOrText(null, text, false);
    }

    @Override
    public void println() {
        printStrOrText(StringUtil.EMPTY, null, true);
    }

    @Override
    public void println(String str) {
        printStrOrText(str, null, true);
    }

    @Override
    public void println(Text text) {
        printStrOrText(null, text, true);
    }

    private List<String> split(String str) {
        List<String> l = new ArrayList<String>();
        if ((chars > 0) && (str.length() + chars > width)) {
            l.add(str.substring(0, width - chars));
            str = str.substring(width - chars);
        }
        while (str.length() > width) {
            l.add(str.substring(0, width));
            str = str.substring(width);
        }
        if (str.length() > 0) {
            l.add(str);
        }
        return l;
    }

    private List<Text> split(Text text) {
        List<Text> l = new ArrayList<Text>();
        if ((chars > 0) && (text.getText().length() + chars > width)) {
            l.add(text.createText(text.getText().substring(0, width - chars)));
            text = text.createText(text.getText().substring(width - chars));
        }
        while (text.getText().length() > width) {
            l.add(text.createText(text.getText().substring(0, width)));
            text = text.createText(text.getText().substring(width));
        }
        if (text.getText().length() > 0) {
            l.add(text);
        }
        return l;
    }

    private void printStrOrText(String str, Text text, boolean newLine) {
        if (lines + 1 > height) {
            return;
        }
        if ((str == null) && ((text == null) || (text.getText() == null))) {
            if (newLine) {
                chars = 0;
                lines++;
                if (lines + 1 > height) {
                    return;
                }
                out.println();
            }
            return;
        }
        if (str != null) {
            List<String> list = split(str);
            if (list.size() == 1) {
                out.print(list.get(0));
                chars += list.get(0).length();
            } else if (list.size() > 1) {
                for (int i = 0; i < list.size(); i++) {
                    String s = list.get(i);
                    if (i < (list.size() - 1)) {
                        lines++;
                        if (lines + 1 > height) {
                            return;
                        }
                    }
                    out.print(s);
                    if (i < (list.size() - 1)) {
                        out.println();
                    }
                }
                chars = list.get(list.size() - 1).length();
            }
        } else {
            List<Text> list = split(text);
            if (list.size() == 1) {
                out.print(list.get(0).toString());
                chars += list.get(0).getText().length();
            } else if (list.size() > 1) {
                for (int i = 0; i < list.size(); i++) {
                    Text t = list.get(i);
                    if (i < (list.size() - 1)) {
                        lines++;
                        if (lines + 1 > height) {
                            return;
                        }
                    }
                    out.print(t);
                    if (i < (list.size() - 1)) {
                        out.println();
                    }
                }
                chars = list.get(list.size() - 1).getText().length();
            }
        }
        if (chars == width) {
            chars = 0;
            lines++;
            if (lines + 1 > height) {
                return;
            }
            out.println();
        }
        if (newLine) {
            chars = 0;
            lines++;
            if (lines + 1 > height) {
                return;
            }
            out.println();
        }
    }

    @Override
    public void finish() {
        while (lines + 1 <= height) {
            println();
        }
        out.println();
        out.flush();
    }
}
