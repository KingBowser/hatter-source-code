package me.hatter.tools.commons.screen.impl;

import me.hatter.tools.commons.color.Text;
import me.hatter.tools.commons.screen.Printer;

public class NormalPrinter implements Printer {

    @Override
    public void init() {
    }

    @Override
    public void print(String str) {
        System.out.print(str);
    }

    @Override
    public void print(Text text) {
        System.out.print(text.toString());
    }

    @Override
    public void println() {
        System.out.println();
    }

    @Override
    public void println(String str) {
        System.out.println(str);
    }

    @Override
    public void println(Text text) {
        System.out.println(text.toString());
    }
}
