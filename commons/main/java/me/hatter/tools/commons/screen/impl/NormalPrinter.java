package me.hatter.tools.commons.screen.impl;

import java.io.PrintStream;

import me.hatter.tools.commons.color.Text;
import me.hatter.tools.commons.screen.Printer;

public class NormalPrinter implements Printer {

    private PrintStream out;

    public NormalPrinter() {
        this(System.out);
    }

    public NormalPrinter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void init() {
    }

    @Override
    public void print(String str) {
        out.print(str);
    }

    @Override
    public void print(Text text) {
        out.print(text.toString());
    }

    @Override
    public void println() {
        out.println();
    }

    @Override
    public void println(String str) {
        out.println(str);
    }

    @Override
    public void println(Text text) {
        out.println(text.toString());
    }

    @Override
    public void finish() {
        out.flush();
    }
}
