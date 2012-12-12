package me.hatter.tools.commons.screen.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import me.hatter.tools.commons.color.Text;
import me.hatter.tools.commons.screen.Printer;

public class BatchOutputScreenPrinter implements Printer {

    private static final String   UTF8 = "UTF-8";

    private ByteArrayOutputStream baos;
    private PrintStream           out;
    private ScreenPrinter         screenPrinter;

    public BatchOutputScreenPrinter(int width, int height) {
        this.baos = new ByteArrayOutputStream();
        try {
            this.out = new PrintStream(baos, false, UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.screenPrinter = new ScreenPrinter(out, width, height);
    }

    @Override
    public void init() {
        screenPrinter.init();
    }

    @Override
    public void print(String str) {
        screenPrinter.print(str);
    }

    @Override
    public void print(Text text) {
        screenPrinter.print(text);
    }

    @Override
    public void println() {
        screenPrinter.println();
    }

    @Override
    public void println(String str) {
        screenPrinter.println(str);
    }

    @Override
    public void println(Text text) {
        screenPrinter.println(text);
    }

    @Override
    public void finish() {
        screenPrinter.finish();
        String output;
        try {
            output = new String(baos.toByteArray(), UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        System.out.print(output);
        System.out.flush();
    }
}
